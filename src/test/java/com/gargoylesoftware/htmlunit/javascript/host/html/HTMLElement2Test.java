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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link HTMLElement}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLElement2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "undefined"})
    public void scopeName() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.body.scopeName);\n"
            + "    log(document.body.tagUrn);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "undefined", "undefined", "http://www.meh.com/meh"})
    public void scopeName2() throws Exception {
        final String html = "<html xmlns:blah='http://www.blah.com/blah'><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var x = document.getElementById('x');\n"
            + "    log(x.scopeName);\n"
            + "    log(x.tagUrn);\n"
            + "    try {\n"
            + "      x.tagUrn = 'http://www.meh.com/meh';\n"
            + "      log(x.scopeName);\n"
            + "      log(x.tagUrn);\n"
            + "    } catch(e) {\n"
            + "      log('error');\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><blah:abc id='x'></blah:abc></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Test offsets (real values don't matter currently).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"number", "number", "number", "number", "number", "number", "number", "number"})
    public void offsets() throws Exception {
        final String html = "<html>\n"
              + "<head></head>\n"
              + "<body>\n"
              + "</div></body>\n"
              + "<div id='div1'>foo</div>\n"
              + "<script>\n"
              + LOG_TITLE_FUNCTION
              + "function alertOffsets(_oElt) {\n"
              + "  log(typeof _oElt.offsetHeight);\n"
              + "  log(typeof _oElt.offsetWidth);\n"
              + "  log(typeof _oElt.offsetLeft);\n"
              + "  log(typeof _oElt.offsetTop);\n"
              + "}\n"
              + "alertOffsets(document.body);\n"
              + "alertOffsets(document.getElementById('div1'));\n"
              + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("attachEvent not available")
    public void offsetWidth_withEvent() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var myDiv2 = document.getElementById('myDiv2');\n"
            + "    if(!document.attachEvent) { log('attachEvent not available'); return }\n"

            + "    myDiv2.attachEvent('ondataavailable', handler);\n"
            + "    document.attachEvent('ondataavailable', handler);\n"
            + "    var m = document.createEventObject();\n"
            + "    m.eventType = 'ondataavailable';\n"
            + "    myDiv2.fireEvent(m.eventType, m);\n"
            + "    document.fireEvent(m.eventType, m);\n"
            + "  }\n"
            + "  function handler() {\n"
            + "    var e = document.getElementById('myDiv');\n"
            + "    e.style.width = 30;\n"
            + "    log(e.offsetWidth);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "  <div id='myDiv2'></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true"})
    public void offsetWidth_spanWithDifferentFontSize() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var myDefault = document.getElementById('myDefault');\n"
            + "    var myLarge = document.getElementById('myLarge');\n"

            + "    log(myDefault.offsetWidth > 20);\n"
            + "    log(myLarge.offsetWidth > myDefault.offsetWidth);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <span id='myDefault'>1234567890</span>\n"
            + "  <span id='myLarge' style='font-size: 10em'>1234567890</span>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true"})
    @NotYetImplemented
    public void offsetWidth_spanWithDifferentFonts() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var mySerif = document.getElementById('mySerif');\n"
            + "    var mySans = document.getElementById('mySans');\n"

            + "    log(mySerif.offsetWidth > 20);\n"
            + "    log(mySans.offsetWidth > mySerif.offsetWidth);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <span id='mySerif' style='font-family: serif'>1234567890</span>\n"
            + "  <span id='mySans' style='font-family: sans-serif'>1234567890</span>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"15", "15"})
    public void offsetTopAndLeft_Padding() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var e = document.getElementById('d');\n"
            + "        log(e.offsetTop);\n"
            + "        log(e.offsetLeft);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()' style='padding: 3px; margin: 0px; border: 0px solid green;'>\n"
            + "    <div style='padding: 5px; margin: 0px; border: 0px solid blue;'>\n"
            + "      <div style='padding: 7px; margin: 0px; border: 0px solid red;'>\n"
            + "        <div id='d' style='padding: 13px; margin: 0px; border: 0px solid black;'>d</div>\n"
            + "      </div>\n"
            + "    </div>\n"
            + "  </body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"13", "28"})
    public void offsetTopAndLeft_Margins() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var e = document.getElementById('d');\n"
            + "        log(e.offsetTop);\n"
            + "        log(e.offsetLeft);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()' style='padding: 0px; margin: 3px; border: 0px solid green;'>\n"
            + "    <div style='padding: 0px; margin: 5px; border: 0px solid blue;'>\n"
            + "      <div style='padding: 0px; margin: 7px; border: 0px solid red;'>\n"
            + "        <div id='d' style='padding: 0px; margin: 13px; border: 0px solid black;'>d</div>\n"
            + "      </div>\n"
            + "    </div>\n"
            + "  </body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"15", "15"},
            FF = {"12", "12"},
            FF78 = {"12", "12"})
    @NotYetImplemented({CHROME, EDGE, IE})
    public void offsetTopAndLeft_Borders() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var e = document.getElementById('d');\n"
            + "        log(e.offsetTop);\n"
            + "        log(e.offsetLeft);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()' style='padding: 0px; margin: 0px; border: 3px solid green;'>\n"
            + "    <div style='padding: 0px; margin: 0px; border: 5px solid blue;'>\n"
            + "      <div style='padding: 0px; margin: 0px; border: 7px solid red;'>\n"
            + "        <div id='d' style='padding: 0px; margin: 0px; border: 13px solid black;'>d</div>\n"
            + "      </div>\n"
            + "    </div>\n"
            + "  </body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "0"})
    public void offsetTopAndLeft_Nothing() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var e = document.getElementById('d');\n"
            + "        log(e.offsetTop);\n"
            + "        log(e.offsetLeft);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()' style='padding: 0px; margin: 0px; border: 0px solid green;'>\n"
            + "    <div style='padding: 0px; margin: 0px; border: 0px solid blue;'>\n"
            + "      <div style='padding: 0px; margin: 0px; border: 0px solid red;'>\n"
            + "        <div id='d' style='padding: 0px; margin: 0px; border: 0px solid black;'>d</div>\n"
            + "      </div>\n"
            + "    </div>\n"
            + "  </body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"50", "50"})
    public void offsetTopAndLeft_AbsolutelyPositioned() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var e = document.getElementById('d');\n"
            + "        log(e.offsetTop);\n"
            + "        log(e.offsetLeft);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <div>\n"
            + "      <div>\n"
            + "        <div id='d' style='position:absolute; top:50; left:50;'>d</div>\n"
            + "      </div>\n"
            + "    </div>\n"
            + "  </body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1 absolute_auto 0", "2 absolute_length 50", "3 absolute_inherit 10", "4 fixed_auto 10",
                "5 fixed_length 50", "6 fixed_inherit 10", "7 relative_auto 0", "8 relative_length 50",
                "9 relative_inherit 10", "10 static_auto 0", "11 static_length 0", "12 static_inherit 0",
                "13 inherit_auto 0", "14 inherit_length 50", "15 inherit_inherit 10"})
    public void offsetLeft_PositionLeft_DifferentCombinations() throws Exception {
        final String html = "<html><body onload='test()'><script language='javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "String.prototype.trim = function() {\n"
            + "  return this.replace(/^\\s+|\\s+$/g, '');\n"
            + "}\n"
            + "function test() {\n"
            + "  var output = document.getElementById('output');\n"
            + "  output.value = '';\n"
            + "  var children = document.getElementById('container').childNodes;\n"
            + "  for(var i = 0; i < children.length; i++) {\n"
            + "    var c = children[i];\n"
            + "    if(c.tagName) output.value += (c.innerHTML + ' ' + c.id + ' ' + c.offsetLeft + '\\n');\n"
            + "  }\n"
            + "  var alerts = output.value.split('\\n');\n"
            + "  for(var i = 0; i < alerts.length; i++) {\n"
            + "    var s = alerts[i].trim();\n"
            + "    if(s) log(s);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "<textarea id='output' cols='40' rows='20'></textarea>\n"
            + "<div id='container' style='position: absolute; left: 10px;'>\n"
            + "  <div id='absolute_auto' style='position: absolute; left: auto;'>1</div>\n"
            + "  <div id='absolute_length' style='position: absolute; left: 50px;'>2</div>\n"
            + "  <div id='absolute_inherit' style='position: absolute; left: inherit;'>3</div>\n"
            + "  <div id='fixed_auto' style='position: fixed; left: auto;'>4</div>\n"
            + "  <div id='fixed_length' style='position: fixed; left: 50px;'>5</div>\n"
            + "  <div id='fixed_inherit' style='position: fixed; left: inherit;'>6</div>\n"
            + "  <div id='relative_auto' style='position: relative; left: auto;'>7</div>\n"
            + "  <div id='relative_length' style='position: relative; left: 50px;'>8</div>\n"
            + "  <div id='relative_inherit' style='position: relative; left: inherit;'>9</div>\n"
            + "  <div id='static_auto' style='position: static; left: auto;'>10</div>\n"
            + "  <div id='static_length' style='position: static; left: 50px;'>11</div>\n"
            + "  <div id='static_inherit' style='position: static; left: inherit;'>12</div>\n"
            + "  <div id='inherit_auto' style='position: inherit; left: auto;'>13</div>\n"
            + "  <div id='inherit_length' style='position: inherit; left: 50px;'>14</div>v>\n"
            + "  <div id='inherit_inherit' style='position: inherit; left: inherit;'>15</div>\n"
            + "</div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"40", "10"})
    public void offsetTopAndLeft_parentAbsolute() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var e = document.getElementById('innerDiv');\n"
            + "    log(e.offsetLeft);\n"
            + "    log(e.offsetTop);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='styleTest' style='position: absolute; left: 400px; top: 50px; padding: 10px 20px 30px 40px;'>\n"
            + "<div id='innerDiv'></div>TEST</div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Minimal flow/layouting test: verifies that the <tt>offsetTop</tt> property changes depending
     * on previous siblings. In the example below, the second div is below the first one, so its
     * offsetTop must be greater than zero. This sort of test is part of the Dojo unit tests, so
     * this needs to pass for the Dojo unit tests to pass.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true", "true", "2", "3", "4", "5", "6", "7", "8", "9", "99", "199", "5999"},
            IE = {"true", "true", "2.0555555555555553", "3.0555555555555553",
                    "4.111111111111111", "5.111111111111111", "6.111111111111111",
                    "7.166666666666667", "8.166666666666666", "9.222222222222221",
                    "101.22222222222223", "203.44444444444446", "6132.333333333333"})
    @NotYetImplemented(IE)
    public void offsetTopWithPreviousSiblings() throws Exception {
        String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('d1').offsetTop == 0);\n"
            + "    var d2OffsetTop = document.getElementById('d2').offsetTop;\n"
            + "    log(d2OffsetTop > 0);\n"

            + "    log(document.getElementById('d3').offsetTop/d2OffsetTop);\n"
            + "    log(document.getElementById('d4').offsetTop/d2OffsetTop);\n"
            + "    log(document.getElementById('d5').offsetTop/d2OffsetTop);\n"
            + "    log(document.getElementById('d6').offsetTop/d2OffsetTop);\n"
            + "    log(document.getElementById('d7').offsetTop/d2OffsetTop);\n"
            + "    log(document.getElementById('d8').offsetTop/d2OffsetTop);\n"
            + "    log(document.getElementById('d9').offsetTop/d2OffsetTop);\n"
            + "    log(document.getElementById('d10').offsetTop/d2OffsetTop);\n"

            + "    log(document.getElementById('d100').offsetTop/d2OffsetTop);\n"
            + "    log(document.getElementById('d200').offsetTop/d2OffsetTop);\n"

            + "    log(document.getElementById('d6000').offsetTop/d2OffsetTop);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body style='padding: 0px; margin: 0px;' onload='test()'>\n";
        for (int i = 1; i <= 6000; i++) {
            html += "  <div id='d" + i + "'>bar</div>\n";
        }
        html = html
            + "</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Partial regression test for Bug #968.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"8", "8"})
    public void offsetTopAndLeftWhenParentIsBody() throws Exception {
        final String html
            = "<html>\n"
            + "  <body onload='var d = document.getElementById(\"d\"); alert(d.offsetLeft); alert(d.offsetTop);'>\n"
            + "    <div id='d'>foo</div>\n"
            + "  </body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for Bug #999.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"23", "19"})
    public void offsetTopAndLeftWithRelativePosition() throws Exception {
        final String html
            = "<html><body onload='test()'><script language='javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var inner = document.createElement('div');\n"
            + "    var outer = document.createElement('div');\n"
            + "    \n"
            + "    document.body.appendChild(outer);\n"
            + "    outer.appendChild(inner);\n"
            + "    \n"
            + "    outer.style.position = 'absolute';\n"
            + "    inner.style.position = 'relative';\n"
            + "    inner.style.left = '19.0px';\n"
            + "    inner.style.top = '23.0px';\n"
            + "    \n"
            + "    log(inner.offsetTop);\n"
            + "    log(inner.offsetLeft);\n"
            + "  }\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"30px", "46", "55px", "71", "71", "0", "0", "0", "0"})
    public void offsetWidthAndHeight() throws Exception {
        final String html =
              "<html><head>\n"
            + "<style>\n"
            + ".dontDisplay { display: none }\n"
            + ".hideMe { visibility: hidden }\n"
            + "</style>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var e = document.getElementById('myDiv');\n"
            + "    e.style.width = 30;\n"
            + "    log(e.style.width);\n"
            + "    log(e.offsetWidth);\n"
            + "    e.style.height = 55;\n"
            + "    log(e.style.height);\n"
            + "    log(e.offsetHeight);\n"
            + "    e.className = 'hideMe';\n"
            + "    log(e.offsetHeight);\n"
            + "    e.className = 'dontDisplay';\n"
            + "    log(e.offsetHeight);\n"
            + "    log(e.offsetWidth);\n"
            + "    var nested = document.getElementById('nested');\n"
            + "    log(nested.offsetHeight);\n"
            + "    log(nested.offsetWidth);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv' style='border: 3px solid #fff; padding: 5px;'><div id='nested'>hello</div></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for Bug #1037.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "0"})
    public void offsetWidthAndHeight_displayNoneAndChildren() throws Exception {
        final String html
            = "<html><body>\n"
            + "<div id='div' style='display: none;'><div style='width: 20px; height: 30px;'></div></div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "log(document.getElementById('div').offsetWidth);</script>\n"
            + "<script>log(document.getElementById('div').offsetHeight);</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for Bug #1290.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "18"})
    public void offsetHeight_explicitHeightZero() throws Exception {
        final String html
            = "<html><body>\n"
            + "<div id='d1' style='height: 0px;'><div id='d2'>x</div></div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "log(document.getElementById('d1').offsetHeight);</script>\n"
            + "<script>log(document.getElementById('d2').offsetHeight);</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Partial regression test for Bug #968.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"75", "2", "5", "20", "50", "50", "18"})
    public void offsetHeight_calculatedBasedOnChildren() throws Exception {
        final String html
            = "<html>\n"
            + "  <body onload='h(\"d1\"); h(\"d2\"); h(\"d3\"); h(\"d4\"); h(\"d5\"); h(\"d6\"); h(\"d7\");'>\n"
            + "    <div id='d1'>\n"
            + "      <div id='d2' style='height:2px;'>x</div>\n"
            + "      <div id='d3' style='height:5px;'><div id='d4' style='height:20px;'>x</div></div>\n"
            + "      <div id='d5'><div id='d6' style='height:50px;'>x</div></div>\n"
            + "      <div id='d7'>x</div>\n"
            + "    </div>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "function h(id) { log(document.getElementById(id).offsetHeight); }</script>\n"
            + "  </body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true"})
    public void offsetHeight_takeFontSizeIntoAccount() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var elem = document.getElementById('myTestDiv');\n"
                + "    var initial = elem.offsetHeight;\n"
                + "    log(initial > 10);\n"
                + "    elem.style.fontSize = '42px';\n"
                + "    log(elem.offsetHeight > initial);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <div id='myTestDiv'>something</div>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Value of offsetWidth is currently wrong when width is a % of the page.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true"})
    public void offsetWidth_calculatedBasedOnPage() throws Exception {
        final String html
            = "<html><body>\n"
            + "<div id='d1' style='width: 20%'>hello</div>\n"
            + "<div><div id='d2' style='width: 20%'>hello</div></div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "log(document.getElementById('d1').offsetWidth > 0);\n"
            + "log(document.getElementById('d2').offsetWidth > 0);\n"
            + "</script></body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("30")
    public void offsetWidth_parentWidthConstrainsChildWidth() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <style>#a { width: 30px; }</style>\n"
            + "</head>\n"
            + "<body>\n"
            + "<div id='a'><div id='b'>foo</div></div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "log(document.getElementById('b').offsetWidth);</script>\n"
            + "</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("30")
    public void offsetWidth_parentWidthConstrainsChildWidth2() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <style>#a{width:30px;} #b{border:2px;padding:3px;}</style>\n"
            + "</head>\n"
            + "<body>\n"
            + "<div id='a'><div id='b'>foo</div></div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "log(document.getElementById('b').offsetWidth);</script>\n"
            + "</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * When CSS float is set to "right" or "left", the width of an element is related to
     * its content and it doesn't takes the full available width.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "0.5", "true"})
    public void offsetWidth_cssFloat_rightOrLeft() throws Exception {
        final String html = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<div id='withoutFloat1'>hello</div><div>hellohello</div>\n"
            + "<div id='withFloat1' style='float: left'>hello</div><div style='float: left'>hellohello</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var eltWithoutFloat1 = document.getElementById('withoutFloat1');\n"
            + "log(eltWithoutFloat1.offsetWidth / eltWithoutFloat1.nextSibling.offsetWidth);\n"
            + "var eltWithFloat1 = document.getElementById('withFloat1');\n"
            + "log(eltWithFloat1.offsetWidth / eltWithFloat1.nextSibling.offsetWidth);\n"
            // we don't make any strong assumption on the screen size here,
            // but expect it to be big enough to show 10 times "hello" on one line
            + "log(eltWithoutFloat1.offsetWidth > 10 * eltWithFloat1.offsetWidth);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true", "true"})
    public void offsetWidth_takeContentIntoAccount() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var elem1 = document.getElementById('myTest1');\n"
                + "    var elem2 = document.getElementById('myTest2');\n"
                + "    var elem3 = document.getElementById('myTest3');\n"
                + "    log(elem1.offsetWidth == 0);\n"
                + "    log(elem1.offsetWidth < elem2.offsetWidth);\n"
                + "    log(elem2.offsetWidth < elem3.offsetWidth);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <span id='myTest1'></span>\n"
                + "  <span id='myTest2'>short</span>\n"
                + "  <span id='myTest3'>loooooooooong</span>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true"})
    public void offsetWidth_takeFontSizeIntoAccount() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var elem = document.getElementById('myTestDiv');\n"
                + "    var initial = elem.offsetWidth;\n"
                + "    log(initial > 10);\n"
                + "    elem.style.fontSize = '42px';\n"
                + "    log(elem.offsetWidth > initial);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <span id='myTestDiv'>something</span>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"something", "0"})
    public void textContent_null() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    checkChildren();\n"
            + "    myTestDiv.textContent = null;\n"
            + "    checkChildren();\n"
            + "  }\n"
            + "  function checkChildren() {\n"
            + "    if (myTestDiv.childNodes.length == 0)\n"
            + "      log('0');\n"
            + "    else\n"
            + "      log(myTestDiv.childNodes.item(0).data);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myTestDiv'>something</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"something", "0"})
    public void textContent_emptyString() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    checkChildren();\n"
            + "    myTestDiv.textContent = '';\n"
            + "    checkChildren();\n"
            + "  }\n"
            + "  function checkChildren() {\n"
            + "    if (myTestDiv.childNodes.length == 0)\n"
            + "      log('0');\n"
            + "    else\n"
            + "      log(myTestDiv.childNodes.item(0).data);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myTestDiv'>something</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"something", "0"},
            IE = {"something", "null"})
    public void innerText_null() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    checkChildren();\n"
            + "    if (myTestDiv.innerText) {\n"
            + "      myTestDiv.innerText = null;\n"
            + "      checkChildren();\n"
            + "    } else {\n"
            + "      log('innerText not supported');\n"
            + "    }\n"
            + "  }\n"
            + "  function checkChildren() {\n"
            + "    if (myTestDiv.childNodes.length == 0)\n"
            + "      log('0');\n"
            + "    else\n"
            + "      log(myTestDiv.childNodes.item(0).data);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myTestDiv'>something</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"something", "0"})
    public void innerText_emptyString() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    checkChildren();\n"
            + "    if (myTestDiv.innerText) {\n"
            + "      myTestDiv.innerText = '';\n"
            + "      checkChildren();\n"
            + "    } else {\n"
            + "      log('innerText not supported');\n"
            + "    }\n"
            + "  }\n"
            + "  function checkChildren() {\n"
            + "    if (myTestDiv.childNodes.length == 0)\n"
            + "      log('0');\n"
            + "    else\n"
            + "      log(myTestDiv.childNodes.item(0).data);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myTestDiv'>something</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Blur isn't fired on DIV elements for instance.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"input handler", "blur input"})
    public void eventHandlerBubble_blur() throws Exception {
        events("blur");
    }

    /**
     * Focus isn't fired on DIV elements for instance.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"input handler", "focus input"})
    public void eventHandlerBubble_focus() throws Exception {
        events("focus");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"input handler", "click input", "div handler", "click div"})
    public void eventHandlerBubble_click() throws Exception {
        events("click");
    }

    private void events(final String type) throws Exception {
        final String html = "<html><head>\n"
            + "</head>\n"
            + "<body>\n"
            + "<div id='div' on" + type + "='log(\"div handler\")'>\n"
            + "<input id='input' on" + type + "='log(\"input handler\")'>\n"
            + "</div>\n"
            + "<textarea id='log'></textarea>\n"
            + "<script>\n"
            + "function log(x) {\n"
            + "  document.getElementById('log').value += x + '\\n';\n"
            + "}\n"
            + "function addListener(id, event) {\n"
            + "  var handler = function(e) { log(event + ' ' + id) };\n"
            + "  var e = document.getElementById(id);\n"
            + "  e.addEventListener(event, handler, false);\n"
            + "}\n"
            + "var eventType = '" + type + "';\n"
            + "addListener('div', eventType);\n"
            + "addListener('input', eventType);\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("input")).click();
        final WebElement log = driver.findElement(By.id("log"));
        log.click();
        final String text = log.getAttribute("value").trim().replaceAll("\r", "");
        assertEquals(String.join("\n", getExpectedAlerts()), text);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "klazz"})
    public void setAttributeNodeUnknown() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var attribute = document.createAttribute('unknown');\n"
            + "    attribute.nodeValue = 'klazz';\n"
            + "    log(document.body.setAttributeNode(attribute));\n"
            + "    log(document.body.getAttributeNode('unknown').nodeValue);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "klazz"})
    public void setAttributeNodeUnknown2() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var attribute = document.createAttribute('unknown');\n"
            + "    log(document.body.setAttributeNode(attribute));\n"
            + "    attribute.nodeValue = 'klazz';\n"
            + "    log(document.body.getAttributeNode('unknown').nodeValue);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "klazz"})
    public void setAttributeNodeClass() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var attribute = document.createAttribute('class');\n"
            + "    attribute.nodeValue = 'klazz';\n"
            + "    log(document.body.setAttributeNode(attribute));\n"
            + "    log(document.body.getAttributeNode('class').nodeValue);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "klazz"})
    public void setAttributeNodeClass2() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var attribute = document.createAttribute('class');\n"
            + "    log(document.body.setAttributeNode(attribute));\n"
            + "    attribute.nodeValue = 'klazz';\n"
            + "    log(document.body.getAttributeNode('class').nodeValue);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "center", "true", "center", "false"})
    public void removeAttributeNode() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var e = document.getElementById('foo');\n"
            + "    log(e.removeAttributeNode != null);\n"
            + "    log(e.getAttribute('align'));\n"
            + "    log(e.hasAttribute('align'));\n"
            + "    var attr = e.getAttributeNode('align');\n"
            + "    log(attr.value);\n"
            + "    e.removeAttributeNode(attr);\n"
            + "    log(e.hasAttribute('align'));\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='foo' align='center' />\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "div1"})
    public void querySelectorAll() throws Exception {
        final String html = "<html><head>\n"
            + "<style>\n"
            + "  .red   {color:#FF0000;}\n"
            + "  .green {color:#00FF00;}\n"
            + "  .blue  {color:#0000FF;}\n"
            + "</style>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var redTags = document.body.querySelectorAll('.green,.red');\n"
            + "  log(redTags.length);\n"
            + "  log(redTags.item(0).id);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1' class='red'>First</div>\n"
            + "  <div id='div2' class='red'>Second</div>\n"
            + "  <div id='div3' class='green'>Third</div>\n"
            + "  <div id='div4' class='blue'>Fourth</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "p1"})
    public void querySelectorAllOnDisconnectedElement() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var myDiv = document.createElement('div');\n"
            + "  myDiv.innerHTML = '<p id=\"p1\" class=\"TEST\"></p>';\n"
            + "  var found = myDiv.querySelectorAll('.TEST');\n"
            + "  log(found.length);\n"
            + "  log(found.item(0).id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void querySelectorAll_badSelector() throws Exception {
        for (final String selector : HTMLDocumentTest.JQUERY_CUSTOM_SELECTORS) {
            doTestQuerySelectorAll_badSelector(selector);
        }

        // some other bad selectors tested in jQuery 1.8.2 tests
        final String[] otherBadSelectors = {":nth-child(2n+-0)", ":nth-child(2+0)",
            ":nth-child(- 1n)", ":nth-child(-1 n)"};
        for (final String selector : otherBadSelectors) {
            doTestQuerySelectorAll_badSelector(selector);
        }
    }

    private void doTestQuerySelectorAll_badSelector(final String selector) throws Exception {
        final String html = "<html><body><div id='it'></div><script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  document.getElementById('it').querySelectorAll('" + selector + "');\n"
            + "  log('working: " + selector + "');\n"
            + "} catch(e) { log('exception'); }\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void querySelector_badSelector() throws Exception {
        for (final String selector : HTMLDocumentTest.JQUERY_CUSTOM_SELECTORS) {
            doTestQuerySelector_badSelector(selector);
        }
    }

    private void doTestQuerySelector_badSelector(final String selector) throws Exception {
        final String html = "<html><body><div id='it'></div><script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  document.getElementById('it').querySelector('" + selector + "');\n"
            + "  log('working');\n"
            + "} catch(e) { log('exception'); }\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Function querySelectorAll should return nodes matched by many rules only once.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void querySelectorAll_noDuplication() throws Exception {
        final String html = "<html><body>\n"
            + "<div><span>First</span></div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var tags = document.body.querySelectorAll('span, div > span');\n"
            + "  log(tags.length);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test the use of innerHTML to set new HTML code.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Old = <b>Old innerHTML</b><!-- old comment -->",
                "New =  <b><i id=\"newElt\">New cell value</i></b>",
                "I"})
    public void getSetInnerHTMLComplex() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "  function log(x) {\n"
            + "    document.getElementById('log').value += x + '\\n';\n"
            + "  }\n"
            + "  function doTest() {\n"
            + "    var myNode = document.getElementById('myNode');\n"
            + "    log('Old = ' + myNode.innerHTML);\n"
            + "    myNode.innerHTML = ' <b><i id=\"newElt\">New cell value</i></b>';\n"
            + "    log('New = ' + myNode.innerHTML);\n"
            + "    log(document.getElementById('newElt').tagName);\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <p id='myNode'><b>Old innerHTML</b><!-- old comment --></p>\n"
            + "  <textarea id='log'></textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        final WebElement log = driver.findElement(By.id("log"));
        final String text = log.getAttribute("value").trim().replaceAll("\r", "");
        assertEquals(String.join("\n", getExpectedAlerts()), text);

        final WebElement pElt = driver.findElement(By.id("myNode"));
        assertEquals("p", pElt.getTagName());

        final WebElement elt = driver.findElement(By.id("newElt"));
        assertEquals("New cell value", elt.getText());
        assertEquals(1, driver.getWindowHandles().size());
    }

    /**
     * Test the use of outerHTML to set new HTML code.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Old = <b id=\"innerNode\">Old outerHTML</b>",
                "New =  <b><i id=\"newElt\">New cell value</i></b>",
                "I"})
    public void getSetOuterHTMLComplex() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "  function doTest() {\n"
            + "    var myNode = document.getElementById('myNode');\n"
            + "    var innerNode = document.getElementById('innerNode');\n"
            + "    alert('Old = ' + innerNode.outerHTML);\n"
            + "    innerNode.outerHTML = ' <b><i id=\"newElt\">New cell value</i></b>';\n"
            + "    alert('New = ' + myNode.innerHTML);\n"
            + "    alert(document.getElementById('newElt').tagName);\n"
            + "  }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<p id='myNode'><b id='innerNode'>Old outerHTML</b></p>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPageWithAlerts2(html);

        final WebElement pElt = driver.findElement(By.id("myNode"));
        assertEquals("p", pElt.getTagName());

        final WebElement elt = driver.findElement(By.id("newElt"));
        assertEquals("New cell value", elt.getText());
        assertEquals(1, driver.getWindowHandles().size());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "true"})
    public void dispatchEvent2() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function simulateClick() {\n"
            + "    var evt = document.createEvent('MouseEvents');\n"
            + "    evt.initMouseEvent('click', true, true, window, 0, 0, 0, 0, 0,"
                        + " false, false, false, false, 0, null);\n"
            + "    var cb = document.getElementById('checkbox');\n"
            + "    cb.dispatchEvent(evt);\n"
            + "  }\n"
            + "  function test() {\n"
            + "    log(document.getElementById('checkbox').checked);\n"
            + "    simulateClick();\n"
            + "    log(document.getElementById('checkbox').checked);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "  <input type='checkbox' id='checkbox'/><label for='checkbox'>Checkbox</label>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test case for issue #1626.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void offsetLeft_PositionFixed() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "  </script>\n"
                + "  <style>\n"
                + "    body {\n"
                + "      padding: 0; margin:0;\n"
                + "    }\n"
                + "    #container {\n"
                + "      width: 200px; position: fixed; right: 0px;\n"
                + "    }\n"
                + "  </style>\n"
                + "</head>\n"
                + "<body onload=\"log(document.getElementById('container').offsetLeft > 0)\">\n"
                + "  <div id=\"container\">\n"
                + "    <ul>\n"
                + "      <li><span>1st</span> List Item.</li>\n"
                + "      <li><span>Another</span> List Item.</li>\n"
                + "    </ul>\n"
                + "  </div>\n"
                + "</body>\n"
                + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"clicked", "fireEvent not available"})
    public void fireEvent_WithoutTemplate() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      var elem = document.getElementById('a');\n"
            + "      if (!elem.fireEvent) { log('fireEvent not available'); return }\n"
            + "      elem.fireEvent('onclick');\n"
            + "    }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "<body>\n"
            + "  <div id='a' onclick='log(\"clicked\")'>foo</div>\n"
            + "  <div id='b' onmouseover='doTest()'>bar</div>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("a")).click();
        verifyTitle2(driver, getExpectedAlerts()[0]);

        final Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(By.id("b")));
        actions.perform();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click", "fireEvent not available", "fireEvent not available"})
    public void fireEvent_WithTemplate() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function dolog(e) {\n"
            + "      log(e.type);\n"
            + "    }\n"

            + "    function doTest() {\n"
            + "      var elem = document.getElementById('a');\n"
            + "      if (!elem.fireEvent) { log('fireEvent not available'); return }\n"
            + "      elem.fireEvent('onclick');\n"
            + "    }\n"

            + "    function doTest2() {\n"
            + "      var elem = document.getElementById('a');\n"
            + "      if (!elem.fireEvent) { log('fireEvent not available'); return }\n"
            + "      var template = document.createEventObject();\n"
            + "      elem.fireEvent('onclick', template);\n"
            + "    }\n"

            + "    </script>\n"
            + "  </head>\n"
            + "<body>\n"
            + "  <div id='a' onclick='dolog(event)'>foo</div>\n"
            + "  <div id='b' onclick='doTest()'>bar</div>\n"
            + "  <div id='c' onclick='doTest2()'>baz</div>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("a")).click();
        verifyTitle2(driver, getExpectedAlerts()[0]);

        driver.findElement(By.id("b")).click();
        verifyTitle2(driver, getExpectedAlerts()[0], getExpectedAlerts()[1]);

        driver.findElement(By.id("c")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Document.write after setting innerHTML.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("hello")
    public void setInnerHTMLDocumentWrite() throws Exception {
        final String html = "<html>\n"
            + "<head><title>test</title></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "     var a = document.createElement('a');\n"
            + "     a.innerHTML = 'break';\n"
            + "     document.write('hello');\n"
            + "</script></body></html>";
        final WebDriver driver = loadPage2(html);
        assertEquals(getExpectedAlerts()[0], driver.findElement(By.tagName("body")).getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"First: body1", "Second:", "Second: body1 setActive not available"},
            IE = {"First: body1", "Second:",
                    "Second: body1 text1 [object HTMLButtonElement] text2 [object Window] onfocus text2"})
    // alert conflicts with focus/blur
    @NotYetImplemented(IE)
    public void setActiveAndFocus() throws Exception {
        final String firstHtml =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <title>First: </title>\n"
            + "  <script>var win2;</script>\n"
            + "</head>\n"
            + "<body id='body1' onload='document.title += \" \" + document.activeElement.id'>\n"
            + "<form name='form1'>\n"
            + "  <input id='text1' onfocus='document.title += \" onfocus text1\"; win2.focus();'>\n"
            + "  <button id='button1' onClick='win2=window.open(\"" + URL_SECOND + "\", \"second\");'>Click me</a>\n"
            + "</form>\n"
            + "</body></html>";

        final String secondHtml =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <title>Second: </title>\n"
            + "</head>\n"
            + "<body id='body2'>\n"
            + "  <input id='text2' onfocus='document.title += \" onfocus text2\"'>\n"
            + "  <button id='button2' onClick='doTest();'>Click me</a>\n"
            + "  <script>\n"
            + "    function doTest() {\n"
            + "      var elem = opener.document.getElementById('text1');\n"
            + "      document.title += ' ' + opener.document.activeElement.id;\n"
            + "      if (!elem.setActive) { document.title += ' setActive not available'; return; }\n"
            + "      elem.setActive();\n"
            + "      document.title += ' ' + opener.document.activeElement.id;\n"
            + "      document.title += ' ' + document.activeElement;\n"
            + "      document.getElementById('text2').setActive();\n"
            + "      document.title += ' ' + document.activeElement.id;\n"
            + "      document.title += ' ' + opener;\n"
            + "      opener.focus();\n"
            + "    }\n"
            + "  </script>\n"
            + "</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, secondHtml);

        try {
            final WebDriver driver = loadPage2(firstHtml);
            assertTitle(driver, getExpectedAlerts()[0]);

            driver.findElement(By.id("button1")).click();

            driver.switchTo().window("second");
            assertTitle(driver, getExpectedAlerts()[1]);

            driver.findElement(By.id("button2")).click();
            assertTitle(driver,  getExpectedAlerts()[2]);
        }
        finally {
            shutDownRealIE();
        }
    }

    /**
     * @throws Exception failure
     */
    @Test
    @Alerts({"DIV,DIV,http://www.w3.org/1999/xhtml,null,div", "svg,svg,http://www.w3.org/2000/svg,null,svg",
            "g,g,http://www.w3.org/2000/svg,null,g", "svg,svg,http://www.w3.org/2000/svg,null,svg"})
    public void variousNames() throws Exception {
        final String html =
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" "
                            + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    debug(document.getElementById('myDiv'));\n"
            + "    debug(document.getElementById('mySVG'));\n"
            + "    debug(document.getElementById('myG'));\n"
            + "    debug(document.getElementById('mySVGWithNS'));\n"
            + "  }\n"
            + "  function debug(e) {\n"
            + "    log(e.nodeName + ',' + e.tagName + ',' + e.namespaceURI + ',' + e.prefix + ',' + e.localName);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id=myDiv>test</div>\n"
            + "  <svg id='mySVG'>\n"
            + "    <G id='myG'></G>\n"
            + "  </svg>\n"
            + "  <svg id='mySVGWithNS' xmlns='http://www.w3.org/2017/svg'>\n"
            + "  </svg>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("success")
    public void setOnclick() throws Exception {
        eventHandlerSetterGetterTest("onclick");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("success")
    public void setOndblclick() throws Exception {
        eventHandlerSetterGetterTest("ondblclick");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("success")
    public void setOnblur() throws Exception {
        eventHandlerSetterGetterTest("onblur");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("success")
    public void setOnfocus() throws Exception {
        eventHandlerSetterGetterTest("onfocus");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("success")
    public void setOnkeydown() throws Exception {
        eventHandlerSetterGetterTest("onkeydown");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("success")
    public void setOnkeypress() throws Exception {
        eventHandlerSetterGetterTest("onkeypress");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("success")
    public void setOnkeyup() throws Exception {
        eventHandlerSetterGetterTest("onkeyup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("success")
    public void setOnmousedown() throws Exception {
        eventHandlerSetterGetterTest("onmousedown");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("success")
    public void setOnmouseup() throws Exception {
        eventHandlerSetterGetterTest("onmouseup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("success")
    public void setOnmouseover() throws Exception {
        eventHandlerSetterGetterTest("onmouseover");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("success")
    public void setOnmouseout() throws Exception {
        eventHandlerSetterGetterTest("onmouseout");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("success")
    public void setOnmousemove() throws Exception {
        eventHandlerSetterGetterTest("onmousemove");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("success")
    public void setOnresize() throws Exception {
        eventHandlerSetterGetterTest("onresize");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("success")
    public void setOnerror() throws Exception {
        eventHandlerSetterGetterTest("onerror");
    }

    /**
     * @param eventName the name of the event
     * @throws Exception if the test fails
     */
    private void eventHandlerSetterGetterTest(final String eventName) throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function handler(event) {}\n"
            + "function test() {\n"
            + "  var oDiv = document.getElementById('myDiv');\n"
            + "  oDiv." + eventName + " = handler;\n"
            + "  if (oDiv." + eventName + " == handler) {\n"
            + "    log('success');\n"
            + "  } else {\n"
            + "    log('fail');\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='myDiv'><br/><div><span>test</span></div></div>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

}
