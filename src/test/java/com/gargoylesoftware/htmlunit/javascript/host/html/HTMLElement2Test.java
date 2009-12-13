/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;

/**
 * Tests for {@link HTMLElement}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HTMLElement2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "HTML", "" }, FF = { "undefined", "undefined" })
    public void scopeName() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.body.scopeName);\n"
            + "    alert(document.body.tagUrn);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "blah", "http://www.blah.com/blah" }, FF = { "undefined", "undefined" })
    public void scopeName2() throws Exception {
        final String html = "<html xmlns:blah='http://www.blah.com/blah'><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('x').scopeName);\n"
            + "    alert(document.getElementById('x').tagUrn);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><blah:abc id='x'></blah:abc></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test offsets (real values don't matter currently).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "number", "number", "number", "number", "number", "number", "number", "number" })
    public void offsets() throws Exception {
        final String html = "<html>\n"
              + "<head>\n"
              + "    <title>Test</title>\n"
              + "</head>\n"
              + "<body>\n"
              + "</div></body>\n"
              + "<div id='div1'>foo</div>\n"
              + "<script>\n"
              + "function alertOffsets(_oElt) {\n"
              + "  alert(typeof _oElt.offsetHeight);\n"
              + "  alert(typeof _oElt.offsetWidth);\n"
              + "  alert(typeof _oElt.offsetLeft);\n"
              + "  alert(typeof _oElt.offsetTop);\n"
              + "}\n"
              + "alertOffsets(document.body);\n"
              + "alertOffsets(document.getElementById('div1'));\n"
              + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "30", "30", "30" })
    public void offsetWidth_withEvent() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var myDiv2 = document.getElementById('myDiv2');\n"
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
            + "    alert(e.offsetWidth);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "  <div id='myDiv2'></div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "15", "15" })
    public void offsetTopAndLeft_Padding() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var e = document.getElementById('d');\n"
            + "        alert(e.offsetTop);\n"
            + "        alert(e.offsetLeft);\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "13", "28" })
    public void offsetTopAndLeft_Margins() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var e = document.getElementById('d');\n"
            + "        alert(e.offsetTop);\n"
            + "        alert(e.offsetLeft);\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "12", "12" })
    public void offsetTopAndLeft_Borders() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var e = document.getElementById('d');\n"
            + "        alert(e.offsetTop);\n"
            + "        alert(e.offsetLeft);\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "0", "0" })
    public void offsetTopAndLeft_Nothing() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var e = document.getElementById('d');\n"
            + "        alert(e.offsetTop);\n"
            + "        alert(e.offsetLeft);\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "50", "50" })
    public void offsetTopAndLeft_AbsolutelyPositioned() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var e = document.getElementById('d');\n"
            + "        alert(e.offsetTop);\n"
            + "        alert(e.offsetLeft);\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(
        FF = { "1 absolute_auto 0", "2 absolute_length 50", "3 absolute_inherit 10", "4 fixed_auto 10",
            "5 fixed_length 50", "6 fixed_inherit 10", "7 relative_auto 0", "8 relative_length 50",
            "9 relative_inherit 10", "10 static_auto 0", "11 static_length 0", "12 static_inherit 0",
            "13 inherit_auto 0", "14 inherit_length 50", "15 inherit_inherit 10" },
        IE = { "1 absolute_auto 0", "2 absolute_length 50", "3 absolute_inherit 0", "4 fixed_auto 0",
            "5 fixed_length 0", "6 fixed_inherit 0", "7 relative_auto 0", "8 relative_length 50",
            "9 relative_inherit 0", "10 static_auto 0", "11 static_length 0", "12 static_inherit 0",
            "13 inherit_auto 0", "14 inherit_length 0", "15 inherit_inherit 0" })
    public void offsetLeft_PositionLeft_DifferentCombinations() throws Exception {
        final String html = "<html><body onload='test()'><script language='javascript'>\n"
            + "String.prototype.trim = function() {\n"
            + "  return this.replace(/^\\s+|\\s+$/g,'');\n"
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
            + "    if(s) alert(s);\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "40", "10" })
    public void offsetTopAndLeft_parentAbsolute() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var e = document.getElementById('innerDiv');\n"
            + "    alert(e.offsetLeft);\n"
            + "    alert(e.offsetTop);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='styleTest' style='position: absolute; left: 400px; top: 50px; padding: 10px 20px 30px 40px;'>"
            + "<div id='innerDiv'></div>TEST</div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
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
    @Alerts({ "false", "true" })
    public void offsetTopWithPreviousSiblings() throws Exception {
        final String html = "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        alert(document.getElementById('d1').offsetTop > 0);\n"
            + "        alert(document.getElementById('d2').offsetTop > 0);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body style='padding: 0px; margin: 0px;' onload='test()'>\n"
            + "    <div id='d1'>foo</div>\n"
            + "    <div id='d2'>bar</div>\n"
            + "  </body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Partial regression test for bug 2892939.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "8", "8" }, IE = { "10", "15" })
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
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "30px", "46", "55px", "71", "71", "0", "0" })
    public void offsetWidthAndHeight() throws Exception {
        final String html =
              "<html><head>\n"
            + "<style>\n"
            + ".dontDisplay { display: none } \n"
            + ".hideMe { visibility: hidden } \n"
            + "</style>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var e = document.getElementById('myDiv');\n"
            + "    e.style.width = 30;\n"
            + "    alert(e.style.width);\n"
            + "    alert(e.offsetWidth);\n"
            + "    e.style.height = 55;\n"
            + "    alert(e.style.height);\n"
            + "    alert(e.offsetHeight);\n"
            + "    e.className = 'hideMe';\n"
            + "    alert(e.offsetHeight);\n"
            + "    e.className = 'dontDisplay';\n"
            + "    alert(e.offsetHeight);\n"
            + "    alert(e.offsetWidth);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv' style='border: 3px solid #fff; padding: 5px;'></div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Partial regression test for bug 2892939.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "77", "2", "5", "20", "50", "50", "20" }, IE = { "100", "15", "20", "20", "50", "50", "15" })
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
            + "    <script>function h(id) { alert(document.getElementById(id).offsetHeight); }</script>\n"
            + "  </body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("30")
    public void offsetWidth_parentWidthConstrainsChildWidth() throws Exception {
        final String html = "<html><head><style>#a { width: 30px; }</style></head><body>\n"
            + "<div id='a'><div id='b'>foo</div></div>\n"
            + "<script>alert(document.getElementById('b').offsetWidth);</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("30")
    public void offsetWidth_parentWidthConstrainsChildWidth2() throws Exception {
        final String html = "<html><head><style>#a{width:30px;} #b{border:2px;padding:3px;}</style></head><body>\n"
            + "<div id='a'><div id='b'>foo</div></div>\n"
            + "<script>alert(document.getElementById('b').offsetWidth);</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * When CSS float is set to "right" or "left", the width of an element is related to
     * its content and it doesn't takes the full available width.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "1", "0.5", "true" })
    public void offsetWidth_cssFloat_rightOrLeft() throws Exception {
        final String html = "<html><head></head><body>\n"
            + "<div id='withoutFloat1'>hello</div><div>hellohello</div>\n"
            + "<div id='withFloat1' style='float: left'>hello</div><div style='float: left'>hellohello</div>\n"
            + "<script>\n"
            + "var eltWithoutFloat1 = document.getElementById('withoutFloat1');\n"
            + "alert(eltWithoutFloat1.offsetWidth / eltWithoutFloat1.nextSibling.offsetWidth);\n"
            + "var eltWithFloat1 = document.getElementById('withFloat1');\n"
            + "alert(eltWithFloat1.offsetWidth / eltWithFloat1.nextSibling.offsetWidth);\n"
            // we don't make any strong assumption on the screen size here,
            // but expect it to be big enough to show 10 times "hello" on one line
            + "alert(eltWithoutFloat1.offsetWidth > 10 * eltWithFloat1.offsetWidth);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "something", "something" }, FF = { "something", "0" })
    public void textContent_null() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    checkChildren();\n"
            + "    myTestDiv.textContent = null;\n"
            + "    checkChildren();\n"
            + "  }\n"
            + "  function checkChildren() {\n"
            + "    if (myTestDiv.childNodes.length == 0)\n"
            + "      alert('0');\n"
            + "    else\n"
            + "      alert(myTestDiv.childNodes.item(0).data);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myTestDiv'>something</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "something", "null" }, FF = { "something", "0" })
    public void innerText_null() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    checkChildren();\n"
            + "    if (myTestDiv.innerText)\n"
            + "      myTestDiv.innerText = null;\n"
            + "    else\n"
            + "      myTestDiv.textContent = null;\n"
            + "    checkChildren();\n"
            + "  }\n"
            + "  function checkChildren() {\n"
            + "    if (myTestDiv.childNodes.length == 0)\n"
            + "      alert('0');\n"
            + "    else\n"
            + "      alert(myTestDiv.childNodes.item(0).data);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myTestDiv'>something</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "something", "0" })
    public void innerText_emptyString() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    checkChildren();\n"
            + "    if (myTestDiv.innerText)\n"
            + "      myTestDiv.innerText = '';\n"
            + "    else\n"
            + "      myTestDiv.textContent = '';\n"
            + "    checkChildren();\n"
            + "  }\n"
            + "  function checkChildren() {\n"
            + "    if (myTestDiv.childNodes.length == 0)\n"
            + "      alert('0');\n"
            + "    else\n"
            + "      alert(myTestDiv.childNodes.item(0).data);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myTestDiv'>something</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Blur isn't fired on DIV elements for instance.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "input handler", "blur input" })
    public void eventHandlerBubble_blur() throws Exception {
        events("blur");
    }

    /**
     * Focus isn't fired on DIV elements for instance.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "input handler", "focus input" })
    public void eventHandlerBubble_focus() throws Exception {
        events("focus");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "input handler", "click input", "div handler", "click div" })
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
            + "  var log = document.getElementById('log');\n"
            + "  log.value += x + '\\n';\n"
            + "}\n"
            + "function addListener(id, event) {\n"
            + "  var handler = function(e) { log(event + ' ' + id) };\n"
            + "  var e = document.getElementById(id);\n"
            + "  if (e.addEventListener) {\n"
            + "    e.addEventListener(event, handler, false)\n"
            + "  } else e.attachEvent('on' + event, handler);\n"
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
        final String text = log.getValue().trim().replaceAll("\r", "");
        assertEquals(StringUtils.join(getExpectedAlerts(), "\n"), text);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "null", "klazz" })
    public void setAttributeNode() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var attribute = document.createAttribute('class');\n"
            + "    attribute.nodeValue = 'klazz';\n"
            + "    alert(document.body.setAttributeNode(attribute));\n"
            + "    alert(document.body.getAttributeNode('class').nodeValue);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageWithAlerts2(html);
    }
}
