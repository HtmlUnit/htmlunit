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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF78;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Set of tests for ill formed HTML code.
 *
 * @author Marc Guillemot
 * @author Sudhan Moghe
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Carsten Steul
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class MalformedHtmlTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"in test", "BODY"})
    public void bodyAttributeWhenOpeningBodyGenerated() throws Exception {
        final String content = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log('in test');\n"
            + "  log(document.getElementById('span1').parentNode.tagName);\n"
            + "}\n"
            + "</script>\n"
            + "<span id='span1'>hello</span>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "3", "text3", "text3", "null"})
    public void lostFormChildren() throws Exception {
        final String content = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.forms[0].childNodes.length);\n"
            + "  log(document.forms[0].elements.length);\n"
            + "  log(document.forms[0].elements[2].name);\n"
            + "  log(document.forms[0].text3.name);\n"
            + "  log(document.getElementById('text4').form);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<div>\n"
            + "<form action='foo'>"
            + "<input type='text' name='text1'/>"
            + "<input type='text' name='text2'/>"
            + "</div>\n"
            + "<input type='text' name='text3'/>\n"
            + "</form>\n"
            + "<input type='text' name='text4' id='text4'/>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Test document")
    public void titleAfterInsertedBody() throws Exception {
        final String content = "<html><head>\n"
            + "<noscript><link href='other.css' rel='stylesheet' type='text/css'></noscript>\n"
            + "<title>Test document</title>\n"
            + "</head><body onload='alert(document.title)'>\n"
            + "foo"
            + "</body></html>";

        loadPageWithAlerts2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Test document")
    public void titleTwice() throws Exception {
        final String content = "<html><head>\n"
            + "<title>Test document</title>\n"
            + "<title>2nd title</title>\n"
            + "</head><body onload='alert(document.title)'>\n"
            + "foo"
            + "</body></html>";

        loadPageWithAlerts2(content);
    }

    /**
     * Test for <a href="https://sourceforge.net/p/nekohtml/bugs/68/">Bug 68</a>.
     * In fact this is not fully correct because IE (6 at least) does something very strange
     * and keeps the DIV in TABLE but wraps it in a node without name.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"DIV", "TABLE"})
    public void div_between_table_and_tr() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var c1 = document.body.firstChild;\n"
            + "  log(c1.tagName);\n"
            + "  log(c1.nextSibling.tagName);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>"
            + "<table><div>hello</div>\n"
            + "<tr><td>world</td></tr></table>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("hello")
    public void script_between_head_and_body() throws Exception {
        final String content = "<html><head><title>foo</title></head><script>\n"
            + "alert('hello');\n"
            + "</script>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(content);
    }

    /**
     * Tests that wrong formed HTML code is parsed like browsers do.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("12345")
    public void wrongHtml_TagBeforeHtml() throws Exception {
        final String html = "<div>\n"
            + "<html>\n"
            + "<head><title>foo</title>\n"
            + "<script>\n"
            + "var toto = 12345;\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='alert(toto)'>\n"
            + "blabla"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertTitle(driver, "foo");
    }

    /**
    * Regression test for bug #889.
    * @throws Exception if an error occurs
    */
    @Test
    @Alerts("0")
    public void missingSingleQuote() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    alert(document.links.length);\n"
                + "  }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  Go to <a href='http://blah.com>blah</a> now.\n"
                + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
    * Regression test for bug #889.
    * @throws Exception if an error occurs
    */
    @Test
    @Alerts("0")
    public void missingDoubleQuote() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    alert(document.links.length);\n"
                + "  }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  Go to <a href=\"http://blah.com>blah</a> now.\n"
                + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
    * Regression test for bug #1192.
    * @throws Exception if an error occurs
    */
    @Test
    @Alerts({"submit", "button"})
    public void brokenInputSingleQuote() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log(document.getElementById('myBody').firstChild.type);\n"
                + "    log(document.getElementById('myBody').firstChild.value);\n"
                + "  }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body id='myBody' onload='test()'>"
                +   "<input width:250px' type='submit' value='button'>"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
    * Regression test for bug #1192.
    * @throws Exception if an error occurs
    */
    @Test
    @Alerts({"submit", "button"})
    public void brokenInputDoubleQuote() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log(document.getElementById('myBody').firstChild.type);\n"
                + "    log(document.getElementById('myBody').firstChild.value);\n"
                + "  }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body id='myBody' onload='test()'>"
                +   "<input width:250px\" type=\"submit\" value=\"button\">"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("inFirst inSecond")
    public void nestedForms() throws Exception {
        final String html = "<html><body>\n"
            + "<form name='TransSearch'>\n"
            + "<input type='submit' id='button'>\n"
            + "<table>\n"
            + "<tr><td><input name='FromDate' value='inFirst'></form></td></tr>\n"
            + "</table>\n"
            + "<table>\n"
            + "<tr><td><form name='ImageSearch'></td></tr>\n"
            + "<tr><td><input name='FromDate' value='inSecond'></form></td></tr>\n"
            + "</table>\n"
            + "<script>\n"
            + "document.title += ' ' + document.forms[0].elements['FromDate'].value;\n"
            + "document.title += ' ' + document.forms[1].elements['FromDate'].value;\n"
            + "</script>\n"
            + "</body></html>";
        final WebDriver driver = loadPage2(html);
        assertTitle(driver, getExpectedAlerts()[0]);

        driver.findElement(By.id("button")).click();
        assertEquals(URL_FIRST + "?FromDate=inFirst", driver.getCurrentUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void li_div_li() throws Exception {
        final String html = "<html><body>\n"
            + "<ul id='it'><li>item 1<div>in div</li><li>item2</li></ul>"
            + "<script>\n"
            + "alert(document.getElementById('it').childNodes.length);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
    * Regression test for bug 1564.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "\uFFFD", "65533"})
    public void entityWithInvalidUTF16Code() throws Exception {
        final String html = "<html><head><title>&#x1b3d6e;</title></head><body><script>"
            + "alert(document.title.length);\n"
            + "alert(document.title);\n"
            + "alert(document.title.charCodeAt(0));\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
    * Regression test for bug 1562.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("hello world")
    public void sectionWithUnknownClosingTag() throws Exception {
        final String html = "<html><body><section id='it'>"
            + "hello</isslot> world"
            + "</section>\n"
            + "<script>\n"
            + "var elt = document.getElementById('it');\n"
            + "alert(elt.textContent || elt.innerText);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"4", "#text:\n  ", "A:null", "DIV:null", "#text:Z\n\n\n", "3",
                "innerDiv", "BODY:null", "3", "A:null", "A:null", "#text:Y",
                "outerA", "BODY:null", "1", "#text:V", "true", "false",
                "outerA", "DIV:null", "1", "#text:W", "false", "false",
                "innerA", "DIV:null", "1", "#text:X", "false", "true"})
    @NotYetImplemented
    // Input:
    // <a id="outerA">V<div id="innerDiv">W<a id="innerA">X</a>Y</div>Z</a>
    // CHROME and IE generate:
    // <a id="outerA">V</a><div id="innerDiv"><a id="outerA">W</a><a id="innerA">X</a>Y</div>Z
    // HtmlUnit generates:
    // <a id="outerA">V<div id="innerDiv">W</div></a><a id="innerA">X</a>YZ
    public void nestedAnchorInDivision() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var outerA = document.getElementById('outerA');\n"
            + "    var innerDiv = document.getElementById('innerDiv');\n"
            + "    var innerA = document.getElementById('innerA');\n"
            + "    var anchors = document.getElementsByTagName('a');\n"

            + "    try {\n"
            + "      log(document.body.childNodes.length);\n"
            + "      dump(document.body.childNodes[0]);\n"
            + "      dump(document.body.childNodes[1]);\n"
            + "      dump(document.body.childNodes[2]);\n"
            + "      dump(document.body.childNodes[3]);\n"
            + "      log(document.getElementsByTagName('a').length);\n"
            + "    } catch (e) { log('exception') }\n"

            + "    try {\n"
            + "      log(innerDiv.id);\n"
            + "      dump(innerDiv.parentNode);\n"
            + "      log(innerDiv.childNodes.length);\n"
            + "      dump(innerDiv.childNodes[0]);\n"
            + "      dump(innerDiv.childNodes[1]);\n"
            + "      dump(innerDiv.childNodes[2]);\n"
            + "    } catch (e) { log('exception') }\n"

            + "    try {\n"
            + "      log(anchors[0].id);\n"
            + "      dump(anchors[0].parentNode);\n"
            + "      log(anchors[0].childNodes.length);\n"
            + "      dump(anchors[0].childNodes[0]);\n"
            + "      log(anchors[0] == outerA);\n"
            + "      log(anchors[0] == innerA);\n"
            + "    } catch (e) { log('exception') }\n"

            + "    try {\n"
            + "      log(anchors[1].id);\n"
            + "      dump(anchors[1].parentNode);\n"
            + "      log(anchors[1].childNodes.length);\n"
            + "      dump(anchors[1].childNodes[0]);\n"
            + "      log(anchors[1] == outerA);\n"
            + "      log(anchors[1] == innerA);\n"
            + "    } catch (e) { log('exception') }\n"

            + "    try {\n"
            + "      log(anchors[2].id);\n"
            + "      dump(anchors[2].parentNode);\n"
            + "      log(anchors[2].childNodes.length);\n"
            + "      dump(anchors[2].childNodes[0]);\n"
            + "      log(anchors[2] == outerA);\n"
            + "      log(anchors[2] == innerA);\n"
            + "    } catch (e) { log('exception') }\n"
            + "  }\n"
            + "  function dump(e) {\n"
            + "    log(e.nodeName + ':' + e.nodeValue);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <a id='outerA'>V<div id='innerDiv'>W<a id='innerA'>X</a>Y</div>Z</a>\n"
            + "</body>\n"
            + "</html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
    * Regression test for bug 1598.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"DOC", "1"})
    public void unknownTagInTable() throws Exception {
        final String html = "<html><body>"
            + "<table id='it'><doc><tr><td>hello</td></tr></doc></table>"
            + "<script>\n"
            + "alert(document.body.firstChild.tagName);\n"
            + "alert(document.getElementById('it').rows.length);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"DOC", "1"})
    public void unknownTagInTbody() throws Exception {
        final String html = "<html><body>"
            + "<table id='it'><tbody><doc><tr><td>hello</td></tr></doc></tbody></table>"
            + "<script>\n"
            + "alert(document.body.firstChild.tagName);\n"
            + "alert(document.getElementById('it').rows.length);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "TABLE", "SCRIPT", "1", "TABLE", "2", "FORM", "TBODY", "0"})
    public void formInTable1() throws Exception {
        final String html = "<html>\n"
                + "<body id='bdy'>\n"
                + "<table>\n"
                + "  <tr>\n"
                + "    <td>\n"
                + "      <table>\n"
                + "        <tr>\n"
                + "          <td id='td0'>\n"
                + "            <table>\n"
                + "              <form id='xyz'>\n"
                + "              <tr>\n"
                + "                <td>\n"
                + "                  <input type='hidden' name='xyz' value='123'>\n"
                + "                  <input type='submit' value='Submit'>\n"
                + "                </td>\n"
                + "              </tr>\n"
                + "              </form>\n"
                + "            </table>"
                + "          </td>\n"
                + "        </tr>\n"
                + "        <tr><td></td></tr>\n"
                + "      </table>\n"
                + "    </td>\n"
                + "  </tr>\n"
                + "</table>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(document.getElementById('bdy').children.length);\n"
                + "  log(document.getElementById('bdy').children[0].tagName);\n"
                + "  log(document.getElementById('bdy').children[1].tagName);\n"

                + "  log(document.getElementById('td0').children.length);\n"
                + "  log(document.getElementById('td0').children[0].tagName);\n"
                + "  log(document.getElementById('td0').children[0].children.length);\n"
                + "  log(document.getElementById('td0').children[0].children[0].tagName);\n"
                + "  log(document.getElementById('td0').children[0].children[1].tagName);\n"

                + "  log(document.getElementById('td0').children[0].children[0].children.length);\n"
                + "</script>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"3", "1a", "1b", "1c", "0", "TBODY", "3", "2a", "2b", "2c", "0", "TBODY"})
    public void formInTable2() throws Exception {
        final String html = "<html>\n"
                + "<body>\n"
                + "<table>\n"
                + "  <tr>\n"
                + "    <td>xyz</td>\n"
                + "  </tr>\n"
                + "  <form name='form1' action='' method='post'>\n"
                + "    <input type='hidden' name='1a' value='a1' />\n"
                + "    <tr>\n"
                + "      <td>\n"
                + "        <table>\n"
                + "          <tr>\n"
                + "            <td>\n"
                + "              <input type='text' name='1b' value='b1' />\n"
                + "            </td>\n"
                + "          </tr>\n"
                + "        </table>\n"
                + "      </td>\n"
                + "    </tr>\n"
                + "    <input type='hidden' name='1c' value='c1'>\n"
                + "  </form>\n"
                + "  <form name='form2' action='' method='post'>\n"
                + "    <input type='hidden' name='2a' value='a2' />\n"
                + "    <tr>\n"
                + "      <td>\n"
                + "        <table>\n"
                + "          <tr>\n"
                + "            <td>\n"
                + "              <input type='text' name='2b' value='b2' />\n"
                + "            </td>\n"
                + "          </tr>\n"
                + "        </table>\n"
                + "      </td>\n"
                + "    </tr>\n"
                + "    <input type='hidden' name='2c' value='c2'>\n"
                + "  </form>\n"
                + "</table>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  for(var i = 0; i < document.forms.length; i++) {\n"
                + "  log(document.forms[i].elements.length);\n"
                + "    for(var j = 0; j < document.forms[i].elements.length; j++) {\n"
                + "      log(document.forms[i].elements[j].name);\n"
                + "    }\n"
                + "    log(document.forms[i].children.length);\n"
                + "    log(document.forms[i].parentNode.tagName);\n"
                + "  }\n"
                + "</script>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"3", "1a", "1b", "", "0", "TABLE"})
    public void formInTable3() throws Exception {
        final String html = "<html>\n"
                + "<body>\n"
                + "  <table>\n"
                + "    <form name='form1' action='' method='get'>\n"
                + "      <input type='hidden' name='1a' value='a1'>\n"
                + "      <tr>\n"
                + "        <td>\n"
                + "          <input type='hidden' name='1b' value='b1'>\n"
                + "          <input type='submit' value='Submit'>\n"
                + "        </td>\n"
                + "      </tr>\n"
                + "    </form>\n"
                + "  </table>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  for(var i = 0; i < document.forms.length; i++) {\n"
                + "  log(document.forms[i].elements.length);\n"
                + "    for(var j = 0; j < document.forms[i].elements.length; j++) {\n"
                + "      log(document.forms[i].elements[j].name);\n"
                + "    }\n"
                + "    log(document.forms[i].children.length);\n"
                + "    log(document.forms[i].parentNode.tagName);\n"
                + "  }\n"
                + "</script>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"3", "1a", "1b", "", "0", "DIV"})
    @NotYetImplemented
    public void formInTable4() throws Exception {
        final String html = "<html>\n"
                + "<body>\n"
                + "  <table>\n"
                + "    <div>\n"
                + "      <form name='form1' action='' method='get'>\n"
                + "        <input type='hidden' name='1a' value='a1'>\n"
                + "        <tr>\n"
                + "          <td>\n"
                + "            <input type='hidden' name='1b' value='b1'>\n"
                + "            <input type='submit' value='Submit'>\n"
                + "          </td>\n"
                + "        </tr>\n"
                + "      </form>\n"
                + "    </div>\n"
                + "  </table>\n"
                + "<script>\n"
                + "  for(var i = 0; i < document.forms.length; i++) {\n"
                + "  alert(document.forms[i].elements.length);\n"
                + "    for(var j = 0; j < document.forms[i].elements.length; j++) {\n"
                + "      alert(document.forms[i].elements[j].name);\n"
                + "    }\n"
                + "    alert(document.forms[i].children.length);\n"
                + "    alert(document.forms[i].parentNode.tagName);\n"
                + "  }\n"
                + "</script>\n"
                + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "1a", "0", "TR", "1", "2a", "0", "TR"})
    public void formInTable5() throws Exception {
        final String html = "<html>\n"
                + "<body>\n"
                + "  <table>\n"
                + "    <tr>\n"
                + "      <form name='form1'>\n"
                + "        <input value='a1' name='1a' type='hidden'></input>\n"
                + "      </form>\n"
                + "      <form name='form2'>\n"
                + "        <input value='a2' name='2a' type='hidden'></input>\n"
                + "      </form>\n"
                + "      <td>\n"
                + "      </td>\n"
                + "    </tr>\n"
                + "  </table>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  for(var i = 0; i < document.forms.length; i++) {\n"
                + "  log(document.forms[i].elements.length);\n"
                + "    for(var j = 0; j < document.forms[i].elements.length; j++) {\n"
                + "      log(document.forms[i].elements[j].name);\n"
                + "    }\n"
                + "    log(document.forms[i].children.length);\n"
                + "    log(document.forms[i].parentNode.tagName);\n"
                + "  }\n"
                + "</script>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "1a", "1b", "0", "TR"})
    public void formInTable6() throws Exception {
        final String html = "<html>\n"
                + "<body>\n"
                + "<table>\n"
                + "  <tr>\n"
                + "    <td></td>\n"
                + "    <form name='form1'>\n"
                + "      <input name='1a' value='a1' type='hidden'></input>\n"
                + "      <td>\n"
                + "        <div>\n"
                + "          <table>\n"
                + "            <tr>\n"
                + "              <td>\n"
                + "                <input name='1b' value='b1'></input>\n"
                + "              </td>\n"
                + "            </tr>\n"
                + "          </table>\n"
                + "        </div>\n"
                + "      </td>\n"
                + "    </form>\n"
                + "  </tr>\n"
                + "</table>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  for(var i = 0; i < document.forms.length; i++) {\n"
                + "  log(document.forms[i].elements.length);\n"
                + "    for(var j = 0; j < document.forms[i].elements.length; j++) {\n"
                + "      log(document.forms[i].elements[j].name);\n"
                + "    }\n"
                + "    log(document.forms[i].children.length);\n"
                + "    log(document.forms[i].parentNode.tagName);\n"
                + "  }\n"
                + "</script>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"3", "1a", "1b", "1c", "0", "TR", "1", "2a", "1", "DIV"})
    public void formInTable7() throws Exception {
        final String html = "<html>\n"
                + "<body>\n"
                + "  <table>\n"
                + "    <tbody>\n"
                + "      <tr>\n"
                + "        <form name='form1'>\n"
                + "          <input type='hidden' name='1a' value='a1' />\n"
                + "          <td>\n"
                + "            <input type='hidden' name='1b' value='b1' />\n"
                + "            <div>\n"
                + "              <input name='1c' value='c1'>\n"
                + "            </div>\n"
                + "          </td>\n"
                + "        </form>\n"
                + "      </tr>\n"
                + "    </tbody>\n"
                + "  </table>\n"
                + "  <div>\n"
                + "    <form name='form2'>\n"
                + "      <input type='hidden' name='2a' value='a2' />\n"
                + "    </form>\n"
                + "  </div>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  for(var i = 0; i < document.forms.length; i++) {\n"
                + "  log(document.forms[i].elements.length);\n"
                + "    for(var j = 0; j < document.forms[i].elements.length; j++) {\n"
                + "      log(document.forms[i].elements[j].name);\n"
                + "    }\n"
                + "    log(document.forms[i].children.length);\n"
                + "    log(document.forms[i].parentNode.tagName);\n"
                + "  }\n"
                + "</script>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "1a", "1b", "2", "BODY", "TR", "TABLE", "2"})
    public void formInTable8() throws Exception {
        final String html = "<html>\n"
                + "<body>\n"
                + "  <form name='form1'>\n"
                + "    <input type='hidden' name='1a' value='a1' />\n"
                + "    <div>\n"
                + "      <table>\n"
                + "        <colgroup id='colgroup'>\n"
                + "          <col width='50%' />\n"
                + "          <col width='50%' />\n"
                + "        </colgroup>\n"
                + "        <thead>\n"
                + "          <tr>\n"
                + "            <th>A</th>\n"
                + "            <th>B</th>\n"
                + "          </tr>\n"
                + "        </thead>\n"
                + "        <tbody>\n"
                + "          <tr>\n"
                + "            <input type='hidden' name='1b' value='b1' />\n"
                + "            <td>1</td>\n"
                + "            <td>2</td>\n"
                + "          </tr>\n"
                + "        </tbody>\n"
                + "      </table>\n"
                + "    </div>\n"
                + "  </form>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(document.form1.elements.length);\n"
                + "  for(var j = 0; j < document.form1.elements.length; j++) {\n"
                + "    log(document.form1.elements[j].name);\n"
                + "  }\n"
                + "  log(document.form1.children.length);\n"
                + "  log(document.form1.parentNode.tagName);\n"
                + "  log(document.form1['1b'].parentNode.tagName);\n"
                + "  log(document.getElementById('colgroup').parentNode.tagName);\n"
                + "  log(document.getElementById('colgroup').children.length);\n"
                + "</script>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"3", "1b", "1a", "1c", "0", "TABLE"},
            IE = {"3", "1a", "1b", "1c", "0", "TABLE"})
    @NotYetImplemented({CHROME, EDGE, FF, FF78})
    public void formInTable9() throws Exception {
        final String html = "<html>\n"
                + "<body>\n"
                + "  <table>\n"
                + "    <form name='form1'>\n"
                + "      <input type='hidden' name='1a' value='a1' />\n"
                + "      <div>\n"
                + "        <input type='hidden' name='1b' value='b1' />\n"
                + "      </div>\n"
                + "      <tbody>\n"
                + "        <tr>\n"
                + "          <input type='hidden' name='1c' value='c1' />\n"
                + "          <td>1</td>\n"
                + "          <td>2</td>\n"
                + "        </tr>\n"
                + "      </tbody>\n"
                + "    </form>\n"
                + "  </table>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(document.form1.elements.length);\n"
                + "  for(var j = 0; j < document.form1.elements.length; j++) {\n"
                + "    log(document.form1.elements[j].name);\n"
                + "  }\n"
                + "  log(document.form1.children.length);\n"
                + "  log(document.form1.parentNode.tagName);\n"
                + "</script>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Test case for issue #1621.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "form1_submit", "0", "TABLE"})
    public void formInTable10() throws Exception {
        final String html = "<html>\n"
                + "<body>\n"
                + "  <table>\n"
                + "    <form name='form1'>\n"
                + "      <tr>\n"
                + "        <td>\n"
                + "          <input name='form1_submit' type='submit'/>\n"
                + "        </td>\n"
                + "      </tr>\n"
                + "    </form>\n"
                + "    <form name='form2'>\n"
                + "    </form>\n"
                + "  </table>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(document.form1.elements.length);\n"
                + "  for(var j = 0; j < document.form1.elements.length; j++) {\n"
                + "    log(document.form1.elements[j].name);\n"
                + "  }\n"
                + "  log(document.form1.children.length);\n"
                + "  log(document.form1.parentNode.tagName);\n"
                + "</script>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"<div>caption</div>", "TABLE"})
    public void nonInlineElementInCaption() throws Exception {
        final String html = "<html>\n"
                + "<body>\n"
                + "  <table>\n"
                + "    <caption id='caption'>\n"
                + "      <div>caption</div>\n"
                + "    </caption>\n"
                + "    <tr>\n"
                + "      <td>content</td>\n"
                + "    </tr>\n"
                + "  </table>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(document.getElementById('caption').innerHTML.replace(/\\s+/g, ''));\n"
                + "  log(document.getElementById('caption').parentNode.tagName);\n"
                + "</script>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "input1", "submit1", "1", "LI", "2", "input2", "submit2", "2", "DIV"})
    public void synthesizedDivInForm() throws Exception {
        final String html = "<html>\n"
                + "<body>\n"
                + "  <ul>\n"
                + "    <li>\n"
                + "      <form name='form1' action='action1' method='POST'>\n"
                + "        <div>\n"
                + "          <input name='input1' value='value1'>\n"
                + "          <input name='submit1' type='submit'>\n"
                + "      </form>\n"
                + "    </li>\n"
                + "  </ul>\n"
                + "  <div>\n"
                + "    <form name='form2' action='action2' method='POST'>\n"
                + "      <input name='input2' value='value2'>\n"
                + "      <input name='submit2' type='submit'>\n"
                + "    </form>\n"
                + "  </div>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  for(var i = 0; i < document.forms.length; i++) {\n"
                + "  log(document.forms[i].elements.length);\n"
                + "    for(var j = 0; j < document.forms[i].elements.length; j++) {\n"
                + "      log(document.forms[i].elements[j].name);\n"
                + "    }\n"
                + "    log(document.forms[i].children.length);\n"
                + "    log(document.forms[i].parentNode.tagName);\n"
                + "  }\n"
                + "</script>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("frame loaded")
    public void siblingWithoutContentBeforeFrameset() throws Exception {
        final String html = "<html>\n"
                + "<div><span></span></div>\n"
                + "<frameset>\n"
                + "  <frame name='main' src='" + URL_SECOND + "' />\n"
                + "</frameset>\n"
                + "</html>";

        final String html2 = "<html><body>\n"
                + "<script>\n"
                + "  alert('frame loaded');\n"
                + "</script>\n"
                + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, html2);
        final WebDriver webDriver = loadPageWithAlerts2(html);
        assertEquals(1, webDriver.findElements(By.name("main")).size());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("frame loaded")
    public void siblingWithWhitespaceContentBeforeFrameset() throws Exception {
        final String html = "<html>\n"
                + "<div>     \t \r \r\n</div>\n"
                + "<frameset>\n"
                + "  <frame name='main' src='" + URL_SECOND + "' />\n"
                + "</frameset>\n"
                + "</html>";

        final String html2 = "<html><body>\n"
                + "<script>\n"
                + "  alert('frame loaded');\n"
                + "</script>\n"
                + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, html2);
        final WebDriver webDriver = loadPageWithAlerts2(html);
        assertEquals(1, webDriver.findElements(By.name("main")).size());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void siblingWithNbspContentBeforeFrameset() throws Exception {
        final String html = "<html>\n"
                + "<div>&nbsp;</div>\n"
                + "<frameset>\n"
                + "  <frame name='main' src='" + URL_SECOND + "' />\n"
                + "</div>\n"
                + "</html>";

        final String html2 = "<html><body>\n"
                + "<script>\n"
                + "  alert('frame loaded');\n"
                + "</script>\n"
                + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, html2);
        final WebDriver webDriver = loadPageWithAlerts2(html);
        assertEquals(0, webDriver.findElements(By.name("main")).size());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void siblingWithContentBeforeFrameset() throws Exception {
        final String html = "<html>\n"
                + "<div><span>CONTENT</span></div>\n"
                + "<frameset>\n"
                + "  <frame name='main' src='" + URL_SECOND + "' />\n"
                + "</frameset>\n"
                + "</html>";

        final String html2 = "<html><body>\n"
                + "<script>\n"
                + "  alert('frame loaded');\n"
                + "</script>\n"
                + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, html2);
        final WebDriver webDriver = loadPageWithAlerts2(html);
        assertEquals(0, webDriver.findElements(By.name("main")).size());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("frame loaded")
    @NotYetImplemented
    public void framesetInsideDiv() throws Exception {
        final String html = "<html>\n"
                + "<div id='tester'>\n"
                + "  <frameset>\n"
                + "    <frame name='main' src='" + URL_SECOND + "' />\n"
                + "  </frameset>\n"
                + "</div>\n"
                + "</html>";

        final String html2 = "<html><body>\n"
                + "<script>\n"
                + "  alert('frame loaded');\n"
                + "</script>\n"
                + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, html2);
        final WebDriver webDriver = loadPageWithAlerts2(html);
        assertEquals(1, webDriver.findElements(By.name("main")).size());
        assertEquals(0, webDriver.findElements(By.id("tester")).size());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("frame loaded")
    @NotYetImplemented
    public void framesetInsideForm() throws Exception {
        final String html = "<html>\n"
                + "<form id='tester'>\n"
                + "  <frameset>\n"
                + "    <frame name='main' src='" + URL_SECOND + "' />\n"
                + "  </frameset>\n"
                + "</form>\n"
                + "</html>";

        final String html2 = "<html><body>\n"
                + "<script>\n"
                + "  alert('frame loaded');\n"
                + "</script>\n"
                + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, html2);
        final WebDriver webDriver = loadPageWithAlerts2(html);
        assertEquals(1, webDriver.findElements(By.name("main")).size());
        assertEquals(0, webDriver.findElements(By.id("tester")).size());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented
    public void framesetInsideTable() throws Exception {
        final String html = "<html>\n"
                + "<table id='tester'>\n"
                + "  <frameset>\n"
                + "    <frame name='main' src='" + URL_SECOND + "' />\n"
                + "  </frameset>\n"
                + "</table>\n"
                + "</html>";

        final String html2 = "<html><body>\n"
                + "<script>\n"
                + "  alert('frame loaded');\n"
                + "</script>\n"
                + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, html2);
        final WebDriver webDriver = loadPageWithAlerts2(html);
        assertEquals(0, webDriver.findElements(By.name("main")).size());
        assertEquals(1, webDriver.findElements(By.id("tester")).size());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§foo?a=1&copy=2&prod=3")
    public void incompleteEntities() throws Exception {
        final String html = "<html><head>\n"
            + "<title>Test document</title>\n"
            + "</head><body>\n"
            + "<a href='foo?a=1&copy=2&prod=3' id='myLink'>my link</a>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("<html><head><title>foo</title></head><body></body></html>");
        expandExpectedAlertsVariables(URL_FIRST);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myLink")).click();

        assertEquals(getExpectedAlerts()[0], getMockWebConnection().getLastWebRequest().getUrl());
    }
}
