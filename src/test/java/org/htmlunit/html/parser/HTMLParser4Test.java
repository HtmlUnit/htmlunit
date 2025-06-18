/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.html.parser;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.html.HtmlPageTest;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

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
            + LOG_TITLE_FUNCTION
            + "log(document.getElementById('tf').parentNode.nodeName);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
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
            + "<html><head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(document.getElementById('myInput').form.id);\n"
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

        loadPageVerifyTitle2(html);
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
            + "    <title></title>\n"
            + "  </head>\n"
            + "  <body>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      var headChildren = document.getElementsByTagName('head')[0].childNodes;\n"
            + "      log(headChildren.length);\n"
            + "      log(headChildren[0]);\n"
            + "      log(headChildren[1]);\n"
            + "      log(headChildren[2]);\n"
            + "      log(headChildren[3]);\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception failure
     */
    @Test
    public void badlyFormedHTML_wrongHeadConfusesStack() throws Exception {
        final String html =
            "<table\n"
            + "<t:head>\n"
            + "<t:head>\n"
            + "<t:head>\n"
            + "<t:head>\n"
            + "<table>\n"
            + "<table>";

        loadPage2(html);
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
            + "    <title></title>\n"
            + "  </head>\n"
            + "  <body>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      var headChildren = document.getElementsByTagName('head')[0].childNodes;\n"
            + "      log(headChildren.length);\n"
            + "      log(headChildren[0]);\n"
            + "      log(headChildren[1]);\n"
            + "      log(headChildren[2]);\n"
            + "      log(headChildren[3]);\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception failure
     */
    @Test
    @Alerts({"4", "[object HTMLParagraphElement]", "[object Text]",
                "[object HTMLScriptElement]", "[object Text]"})
    @HtmlUnitNYI(CHROME = {"3", "[object HTMLParagraphElement]", "[object HTMLScriptElement]",
                           "[object Text]", "undefined"},
            EDGE = {"3", "[object HTMLParagraphElement]", "[object HTMLScriptElement]",
                    "[object Text]", "undefined"},
            FF = {"3", "[object HTMLParagraphElement]", "[object HTMLScriptElement]",
                  "[object Text]", "undefined"},
            FF_ESR = {"3", "[object HTMLParagraphElement]", "[object HTMLScriptElement]",
                      "[object Text]", "undefined"})
    public void badlyFormedHTML_scriptAfterHtml() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var headChildren = document.getElementsByTagName('body')[0].childNodes;\n"
            + "        log(headChildren.length);\n"
            + "        log(headChildren[0]);\n"
            + "        log(headChildren[1]);\n"
            + "        log(headChildren[2]);\n"
            + "        log(headChildren[3]);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'><p>HtmlUnit</p></body>\n"
            + "</html>"
            + "<script>var i = 7;</script>\n";

        loadPageVerifyTitle2(html);
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
            + "    <script src='script.js'></script>\n"
            + "  </head>\n"
            + "  </head>\n"
            + "  <body>\n"
            + "    <p>HtmlUnit</p>\n"
            + "    <script>window.name += 'from body' + '\\u00a7';</script>\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("window.name += 'from script' + '\\u00a7'", 200, "OK", null);

        loadPage2(html);
        verifyWindowName2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception failure
     */
    @Test
    @Alerts("<html><head><title>foo</title></head>"
            + "<body><script>window.name += document.documentElement.outerHTML + '\\u00a7';</script></body></html>")
    public void badlyFormedHTML_duplicateHeadStructure() throws Exception {
        shutDownAll();

        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>"
            + "<head>"
            + "<head>"
            + "<title>foo</title>"
            + "</head>"
            + "</head>"
            + "<body>"
            + "<script>window.name += document.documentElement.outerHTML + '\\u00a7';</script>"
            + "</body>"
            + "</html>";

        loadPage2(html);
        verifyWindowName2(getWebDriver(), getExpectedAlerts());
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
            + "  </head>\n"
            + "  <body>\n"
            + "    <div id='tester'><p title='Nimbus&#84823000 X'>Nimbus&#84823000 X</p></div>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      log(document.getElementById('tester').innerHTML);\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
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
            + LOG_WINDOW_NAME_FUNCTION
            + "    function test() {\n"
            + "      log(document.title);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPage2(html);
        verifyWindowName2(getWebDriver(), getExpectedAlerts());
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
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(document.getElementById('foo') == null);\n"
            + "      log(document.getElementById('bla') == null);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <span id='foo' id='bla'></span>"
            + "</body></html>";

        loadPageVerifyTitle2(html);
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
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('script1'));\n"
            + "    log(document.getElementById('script2'));\n"
            + "    log(document.getElementById('message1'));\n"
            + "    log(document.getElementById('form1'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<script id='script1'>log(1)</script>\n"
            + "<app:script id='script2'>log(2)</app:script>\n"
            + "<script>log(3)</script>\n"
            + "<app:message name='r:tasks.request' id='message1'>hello</app:message>\n"
            + "<form id='form1' xmlns='http://org.pentaho'/>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
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
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      div = document.createElementNS('http://www.appcelerator.org', 'app:script');\n"
            + "      debug(div);\n"
            + "    } catch(e) {log('createElementNS() is not defined')}\n"
            + "    debug(document.getElementById('script1'));\n"
            + "    debug(document.getElementById('script2'));\n"
            + "  }\n"
            + "  function debug(e) {\n"
            + "    log(e);\n"
            + "    log(e.nodeName + ',' + e.tagName + ',' + e.namespaceURI + ',' + e.prefix + ',' + e.localName);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<script id='script1'>log(1)</script>\n"
            + "<app:script id='script2'>log(2)</app:script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * See issue #1830.
     * @throws Exception failure
     */
    @Test
    @Alerts({"[object HTMLHeadElement]",
                "HEAD,HEAD,http://www.w3.org/1999/xhtml,null,head",
                "[object HTMLBodyElement]", "BODY,BODY,http://www.w3.org/1999/xhtml,null,body"})
    public void namespace_svg() throws Exception {
        final String html =
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" "
                            + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
            + "<html xmlns=\"http://www.w3.org/2000/svg\">\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    elem = document.getElementsByTagName('head')[0];\n"
            + "    debug(elem);\n"
            + "    elem = document.getElementsByTagName('body')[0];\n"
            + "    debug(elem);\n"
            + "  }\n"
            + "  function debug(e) {\n"
            + "    log(e);\n"
            + "    log(e.nodeName + ',' + e.tagName + ',' + e.namespaceURI + ',' + e.prefix + ',' + e.localName);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

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
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    elem = document.getElementsByTagName('head')[0];\n"
            + "    debug(elem);\n"
            + "    elem = document.getElementsByTagName('body')[0];\n"
            + "    debug(elem);\n"
            + "    elem = document.getElementById('rectangles');\n"
            + "    debug(elem);\n"
            + "  }\n"
            + "  function debug(e) {\n"
            + "    log(e);\n"
            + "    log(e.nodeName + ',' + e.tagName + ',' + e.namespaceURI + ',' + e.prefix + ',' + e.localName);\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * See issue #719.
     * @throws Exception failure
     */
    @Test
    @Alerts("5")
    public void svg_selfClosingTags() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    elem = document.getElementById('tester');\n"
            + "    log(elem.childNodes.length);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <svg id='tester' xmlns=\"http://www.w3.org/2000/svg\">\n"
            + "    <path fill=\"#4285F4\" />\n"
            + "    <path fill=\"#34A853\" />\n"
            + "  </svg>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
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
    public void completeHtmlInsideDiv() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "  <title>Outer Html</title>\n"
            + "  <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "    function test() {\n"
            + "      log('titles');\n"
            + "      var titles = document.getElementsByTagName('title');\n"
            + "      for(var i = 0; i < titles.length; i++) {\n"
            + "        log(titles[i].parentNode.nodeName);\n"
            + "        log(titles[i].text);\n"
            + "      }\n"
            + "      log('bodyTitles');\n"
            + "      var bodyTitles = document.body.getElementsByTagName('title');\n"
            + "      for(var i = 0; i < bodyTitles.length; i++) {\n"
            + "        log(bodyTitles[i].parentNode.nodeName);\n"
            + "        log(bodyTitles[i].text);\n"
            + "      }\n"
            + "      log('innerDiv');\n"
            + "      var innerDiv = document.getElementById('innerDiv');\n"
            + "      log(innerDiv.parentNode.id);\n"
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
            + LOG_TEXTAREA
            + "</body>\n"
            + "</html>\n";

        loadPageVerifyTextArea2(html);
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
    public void writeCompleteHtmlInsideDIV() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "  <title>Outer Html</title>\n"
            + "  <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "    function test() {\n"
            + "      log('titles');\n"
            + "      var titles = document.getElementsByTagName('title');\n"
            + "      for(var i = 0; i < titles.length; i++) {\n"
            + "        log(titles[i].parentNode.nodeName);\n"
            + "        log(titles[i].text);\n"
            + "      }\n"
            + "      log('bodyTitles');\n"
            + "      var bodyTitles = document.body.getElementsByTagName('title');\n"
            + "      for(var i = 0; i < bodyTitles.length; i++) {\n"
            + "        log(bodyTitles[i].parentNode.nodeName);\n"
            + "        log(bodyTitles[i].text);\n"
            + "      }\n"
            + "      log('innerDiv');\n"
            + "      var innerDiv = document.getElementById('innerDiv');\n"
            + "      log(innerDiv.parentNode.id);\n"
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
            + LOG_TEXTAREA
            + "</body>\n"
            + "</html>\n";

        loadPageVerifyTextArea2(html);
    }

    /**
     * Test for a case where complete HTML page is set in innerHTML of DIV tag.
     * Behavior is same for any TAG inside body including BODY tag.
     * Browsers ignore only HTML, HEAD and BODY tags. Contents of BODY are added to
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
            + LOG_TEXTAREA_FUNCTION
            + "    function test() {\n"
            + "      log('titles');\n"
            + "      var titles = document.getElementsByTagName('title');\n"
            + "      for(var i = 0; i < titles.length; i++) {\n"
            + "        log(titles[i].parentNode.nodeName);\n"
            + "        log(titles[i].text);\n"
            + "      }\n"
            + "      log('bodyTitles');\n"
            + "      var bodyTitles = document.body.getElementsByTagName('title');\n"
            + "      for(var i = 0; i < bodyTitles.length; i++) {\n"
            + "        log(bodyTitles[i].parentNode.nodeName);\n"
            + "        log(bodyTitles[i].text);\n"
            + "      }\n"
            + "      log('innerDiv');\n"
            + "      var innerDiv = document.getElementById('innerDiv');\n"
            + "      log(innerDiv.parentNode.id);\n"
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
            + LOG_TEXTAREA
            + "</body>\n"
            + "</html>\n";

        loadPageVerifyTextArea2(html);
    }

    /**
     * Test for a case where complete HTML page is set in innerHTML of DIV tag.
     * Behavior is same for any TAG inside body including BODY tag.
     * Browsers ignore only HTML, HEAD and BODY tags. Contents of BODY are added to
     * the current node (DIV tag in test case).
     *
     * @throws Exception failure
     */
    @Test
    @Alerts({"titles", "HEAD", "Outer Html", "DIV", "Inner Html",
                "bodyTitles", "DIV", "Inner Html",
                "innerDiv", "outerDiv"})
    public void setCompleteHtmlToDIV2_innerHTML() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "  <title>Outer Html</title>\n"
            + "  <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "    function test() {\n"
            + "      log('titles');\n"
            + "      var titles = document.getElementsByTagName('title');\n"
            + "      for(var i = 0; i < titles.length; i++) {\n"
            + "        log(titles[i].parentNode.nodeName);\n"
            + "        log(titles[i].text);\n"
            + "      }\n"
            + "      log('bodyTitles');\n"
            + "      var bodyTitles = document.body.getElementsByTagName('title');\n"
            + "      for(var i = 0; i < bodyTitles.length; i++) {\n"
            + "        log(bodyTitles[i].parentNode.nodeName);\n"
            + "        log(bodyTitles[i].text);\n"
            + "      }\n"
            + "      log('innerDiv');\n"
            + "      var innerDiv = document.getElementById('innerDiv');\n"
            + "      log(innerDiv.parentNode.id);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <DIV id=outerDiv>\n"
            + "     Outer DIV\n"
            + "  </DIV>\n"
            + "    <script>\n"
            + "    document.getElementById('outerDiv').innerHTML ="
            + "        '<html><head>"
            + "            <title>Inner Html</title>"
            + "            <meta name=\"author\" content=\"John Doe\">"
            + "        </head>"
            + "        <body><DIV id=innerDiv>Inner DIV</DIV></body></html>';\n"
            + "    </script>\n"
            + LOG_TEXTAREA
            + "</body>\n"
            + "</html>\n";

        loadPageVerifyTextArea2(html);
    }

    /**
     * Test for a case where complete HTML page is set in innerHTML of HTML tag.
     * Others replace the current content of the HTML node by the new one.
     *
     * @throws Exception failure
     */
    @Test
    @Alerts({"titles", "HEAD", "Inner Html", "misc", "true", "BODY"})
    @HtmlUnitNYI(CHROME = {"titles", "HTML", "Inner Html", "misc", "false", "HTML"},
            EDGE = {"titles", "HTML", "Inner Html", "misc", "false", "HTML"},
            FF = {"titles", "HTML", "Inner Html", "misc", "false", "HTML"},
            FF_ESR = {"titles", "HTML", "Inner Html", "misc", "false", "HTML"})
    // currently the content of HEAD and BODY are added directly to HTML
    public void setCompleteHtmlToHTML_innerHTML() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "  <title>Outer Html</title>\n"
            + "  <script>\n"
            + LOG_WINDOW_NAME_FUNCTION
            + "    function test() {\n"
            + "      log('titles');\n"
            + "      var titles = document.getElementsByTagName('title');\n"
            + "      for(var i = 0; i < titles.length; i++) {\n"
            + "        log(titles[i].parentNode.nodeName);\n"
            + "        log(titles[i].text);\n"
            + "      }\n"
            + "      log('misc');\n"
            + "      log(document.body != null);\n"
            + "      var innerDiv = document.getElementById('innerDiv');\n"
            + "      if (innerDiv != null) {\n"
            + "        log(innerDiv.parentNode.nodeName);\n"
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
            + "    } catch(e) { logEx(e) }\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>\n";

        loadPage2(html);
        verifyWindowName2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception failure
     */
    @Test
    @Alerts("before1after1\\nbefore2after2\\nbefore3after3\\nbefore4after4\\nbefore5after5\\nbefore6<>after6")
    @HtmlUnitNYI(
            CHROME = "before1after1\\sbefore2after2\\sbefore3after3\\sbefore4after4\\sbefore5after5\\sbefore6<>after6",
            EDGE = "before1after1\\sbefore2after2\\sbefore3after3\\sbefore4after4\\sbefore5after5\\sbefore6<>after6",
            FF = "before1after1\\sbefore2after2\\sbefore3after3\\sbefore4after4\\sbefore5after5\\sbefore6<>after6",
            FF_ESR = "before1after1\\sbefore2after2\\sbefore3after3\\sbefore4after4\\sbefore5after5\\sbefore6<>after6")
    public void specialComments() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION_NORMALIZE
                + "    function test() {\n"
                + "      var body = document.getElementById('tester');\n"
                + "      var text = body.innerText;"
                + "      log(text);\n"
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

        loadPageVerifyTitle2(html, getExpectedAlerts());
    }

    /**
     * @throws Exception failure
     */
    @Test
    @Alerts("before1after1\\nbefore2\\nbefore3\\nbefore4after4\\nbefore5after5\\nbefore6<\\s>after6")
    @HtmlUnitNYI(CHROME = "before1after1\\sbefore2\\sbefore3\\sbefore4after4\\sbefore5after5\\sbefore6<\\s>after6",
            EDGE = "before1after1\\sbefore2\\sbefore3\\sbefore4after4\\sbefore5after5\\sbefore6<\\s>after6",
            FF = "before1after1\\sbefore2\\sbefore3\\sbefore4after4\\sbefore5after5\\sbefore6<\\s>after6",
            FF_ESR = "before1after1\\sbefore2\\sbefore3\\sbefore4after4\\sbefore5after5\\sbefore6<\\s>after6")
    public void specialComments2() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION_NORMALIZE
                + "    function test() {\n"
                + "      var body = document.getElementById('tester');\n"
                + "      var text = body.innerText;"
                + "      log(text);\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception failure
     */
    @Test
    @Alerts({"1", "[object HTMLTemplateElement]", "[object DocumentFragment]",
             "[object HTMLTableElement]", "[object HTMLTableSectionElement]"})
    public void tableInsideTemplate_addTBody() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var bodyChildren = document.getElementsByTagName('body')[0].childNodes;\n"
            + "        log(bodyChildren.length);\n"
            + "        log(bodyChildren[0]);\n"
            + "        log(bodyChildren[0].content);\n"

            + "        if(bodyChildren[0].content === undefined) { return; }"
            + "        var table = bodyChildren[0].content.firstElementChild;\n"
            + "        log(table);\n"
            + "        log(table.firstChild);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'><template><table><tr><td>42</td></tr></table></template></body>"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception failure
     */
    @Test
    @Alerts({"1", "[object HTMLTemplateElement]", "[object DocumentFragment]",
             "[object HTMLUListElement]"})
    public void tableInsideTemplate_addTBodyMoveUlOut() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var bodyChildren = document.getElementsByTagName('body')[0].childNodes;\n"
            + "        log(bodyChildren.length);\n"
            + "        log(bodyChildren[0]);\n"
            + "        log(bodyChildren[0].content);\n"

            + "        if(bodyChildren[0].content === undefined) { return; }"
            + "        var ul = bodyChildren[0].content.firstElementChild;\n"
            + "        log(ul);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'><template><table><ul></ul><tr><td>42</td></tr></table></template></body>"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception failure
     */
    @Test
    @Alerts({"1", "[object HTMLElement]", "1", "[object Text]",
             "<!-- anchor linking to external file -->"
                     + "<a href='https://www.htmlunit.org/'>External Link</a>"})
    public void noscript() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var bodyChildren = document.getElementsByTagName('body')[0].childNodes;\n"
            + "        log(bodyChildren.length);\n"
            + "        log(bodyChildren[0]);\n"

            + "        var noscript = bodyChildren[0];\n"
            + "        log(noscript.childNodes.length);\n"
            + "        log(noscript.childNodes[0]);\n"
            + "        log(noscript.childNodes[0].textContent);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>"
                    + "<noscript>"
                    + "<!-- anchor linking to external file -->"
                    + "<a href='https://www.htmlunit.org/'>External Link</a>"
                    + "</noscript>"
            + "</body>"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception failure
     */
    @Test
    @Alerts({"3", "[object HTMLElement]", "[object HTMLDivElement]", "[object Text]",
             "1", "[object Text]", "<!-- ",
             "1", "<div>abc</div>",
             "0", "-->"})
    public void noscriptBrokenComment() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var bodyChildren = document.getElementsByTagName('body')[0].childNodes;\n"
            + "        log(bodyChildren.length);\n"
            + "        log(bodyChildren[0]);\n"
            + "        log(bodyChildren[1]);\n"
            + "        log(bodyChildren[2]);\n"

            + "        var noscript = bodyChildren[0];\n"
            + "        log(noscript.childNodes.length);\n"
            + "        log(noscript.childNodes[0]);\n"
            + "        log(noscript.childNodes[0].textContent);\n"

            + "        var div = bodyChildren[1];\n"
            + "        log(div.childNodes.length);\n"
            + "        log(div.outerHTML);\n"

            + "        var txt = bodyChildren[2];\n"
            + "        log(txt.childNodes.length);\n"
            + "        log(txt.textContent);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>"
                    + "<noscript>"
                    + "<!-- </noscript><div>abc</div>-->"
                    + "</noscript>"
            + "</body>"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts("ti </head> <body> 1234 </body> </html>")
    public void badTagInHead() throws Exception {
        final String html =
                "<html>\n"
                + "<head><foo/>\n"
                + "<title>ti\n"
                + "</head>\n"
                + "<body>\n"
                + "1234\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html);
        assertEquals(getExpectedAlerts()[0], driver.getTitle());
    }
}
