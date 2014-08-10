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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Unit tests for {@link CSSStyleSheet}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class CSSStyleSheetTest extends WebDriverTestCase {

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"[object CSSStyleSheet]", "[object HTMLStyleElement]", "true", "undefined", "false" },
            IE8 = {"[object]", "undefined", "false", "[object]", "true" },
            IE11 = {"[object CSSStyleSheet]", "[object HTMLStyleElement]",
                    "true", "[object HTMLStyleElement]", "true" })
    public void owningNodeOwningElement() throws Exception {
        final String html = "<html><head><title>test_hasChildNodes</title>\n"
                + "<script>\n"
                + "function test(){\n"
                + "  var myStyle = document.getElementById('myStyle');\n"
                + "  var stylesheet = document.styleSheets[0];\n"
                + "  alert(stylesheet);\n"
                + "  alert(stylesheet.ownerNode);\n"
                + "  alert(stylesheet.ownerNode == myStyle);\n"
                + "  alert(stylesheet.owningElement);\n"
                + "  alert(stylesheet.owningElement == myStyle);\n"
                + "}\n"
                + "</script>\n"
                + "<style id='myStyle' type='text/css'></style>\n"
                + "</head><body onload='test()'>\n"
                + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = { "4", "0", "1", "2", "3", "length", "item" },
            IE8 = { "4", "length", "0", "1", "2", "3" })
    public void rules() throws Exception {
        final String html = "<html><head><title>First</title>\n"
                + "<style>\n"
                + "  BODY { background-color: white; color: black; }\n"
                + "  H1 { font: 8pt Arial bold; }\n"
                + "  P  { font: 10pt Arial; text-indent: 0.5in; }\n"
                + "  A  { text-decoration: none; color: blue; }\n"
                + "</style>\n"
                + "<script>\n"
                + "  function test(){\n"
                + "    var rules;\n"
                + "    if (document.styleSheets[0].cssRules)\n"
                + "      rules = document.styleSheets[0].cssRules;\n"
                + "    else\n"
                + "      rules = document.styleSheets[0].rules;\n"
                + "    alert(rules.length);\n"
                + "    for (var i in rules)\n"
                + "      alert(i);\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test for bug 2063012 (missing href attribute).
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "4", "§§URL§§style2.css", "§§URL§§style4.css", "null", "null" },
            IE8 = { "4", "§§URL§§style2.css", "style4.css", "", "" })
    public void href() throws Exception {
        final String baseUrl = getDefaultUrl().toExternalForm();
        final String html = "<html>\n"
            + "  <head>\n"
            + "    <link href='" + baseUrl + "style1.css' type='text/css'></link>\n" // Ignored.
            + "    <link href='" + baseUrl + "style2.css' rel='stylesheet'></link>\n"
            + "    <link href='" + baseUrl + "style3.css'></link>\n" // Ignored.
            + "    <link href='style4.css' rel='stylesheet'></link>\n"
            + "    <style>div.x { color: red; }</style>\n"
            + "  </head>\n" + "  <body>\n"
            + "    <style>div.y { color: green; }</style>\n"
            + "    <script>\n"
            + "      alert(document.styleSheets.length);\n"
            + "      alert(document.styleSheets[0].href);\n"
            + "      alert(document.styleSheets[1].href);\n"
            + "      alert(document.styleSheets[2].href);\n"
            + "      alert(document.styleSheets[3].href);\n"
            + "    </script>\n" + "  </body>\n"
            + "</html>";

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(new URL(getDefaultUrl(), "style1.css"), "");
        conn.setResponse(new URL(getDefaultUrl(), "style2.css"), "");
        conn.setResponse(new URL(getDefaultUrl(), "style3.css"), "");
        conn.setResponse(new URL(getDefaultUrl(), "style4.css"), "");

        loadPageWithAlerts2(html, new URL(getDefaultUrl(), "test.html"));
    }

    /**
     * Minimal test for addRule / insertRule.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "1", "false", "false", "0", "2", "p" },
            FF = { "1", "false", "true", "0", "2", "p" },
            IE8 = { "1", "true", "false", "-1", "2", "DIV" })
    public void addRule_insertRule() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var f = document.getElementById('myStyle');\n"
            + "  var s = f.sheet ? f.sheet : f.styleSheet;\n"
            + "  var rules = s.cssRules || s.rules;\n"
            + "  alert(rules.length);\n"
            + "  alert(s.insertRule == undefined);\n"
            + "  alert(s.addRule == undefined);\n"
            + "  if (s.insertRule)\n"
            + "    alert(s.insertRule('div { color: red; }', 0));\n"
            + "  else\n"
            + "    alert(s.addRule('div', 'color: red;', 1));\n"
            + "  alert(rules.length);\n"
            + "  alert(rules[1].selectorText);\n"
            + "}</script>\n"
            + "<style id='myStyle'>p { vertical-align:top }</style>\n"
            + "</head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Minimal test for removeRule / deleteRule.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "2", "false", "true", "undefined", "1", "div" },
            IE = { "2", "true", "false", "undefined", "1", "DIV" },
            IE11 = { "2", "false", "false", "undefined", "1", "div" },
            CHROME = { "2", "false", "false", "undefined", "1", "div" })
    public void removeRule_deleteRule() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var f = document.getElementById('myStyle');\n"
            + "  var s = f.sheet ? f.sheet : f.styleSheet;\n"
            + "  var rules = s.cssRules || s.rules;\n"
            + "  alert(rules.length);\n"
            + "  alert(s.deleteRule == undefined);\n"
            + "  alert(s.removeRule == undefined);\n"
            + "  if (s.deleteRule)\n"
            + "    alert(s.deleteRule(0));\n"
            + "  else\n"
            + "    alert(s.removeRule(0));\n"
            + "  alert(rules.length);\n"
            + "  alert(rules[0].selectorText);\n"
            + "}</script>\n"
            + "<style id='myStyle'>p { vertical-align:top } div { color: red; }</style>\n"
            + "</head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test that exception handling in insertRule.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void deleteRuleInvalidParam() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var f = document.getElementById('myStyle');\n"
            + "  var s = f.sheet ? f.sheet : f.styleSheet;\n"
            + "  var rules = s.cssRules || s.rules;\n"
            + "  try {\n"
            + "    if (s.deleteRule)\n"
            + "      s.deleteRule(19);\n"
            + "    else\n"
            + "      s.removeRule(19);\n"
            + "    alert('deleted');\n"
            + "  } catch(err) { alert('exception'); }\n"
            + "}</script>\n"
            + "<style id='myStyle'></style>\n"
            + "</head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test that CSSParser can handle leading whitespace in insertRule.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "2", ".testStyleDef", ".testStyle" }, IE8 = { },
            CHROME = { "2", ".teststyledef", ".teststyle" })
    public void insertRuleLeadingWhitespace() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var f = document.getElementById('myStyle');\n"
            + "  var s = f.sheet ? f.sheet : f.styleSheet;\n"
            + "  var rules = s.cssRules || s.rules;\n"
            + "  if (s.insertRule) {\n"
            + "    s.insertRule('.testStyle { width: 24px; }', 0);\n"
            + "    s.insertRule(' .testStyleDef { height: 42px; }', 0);\n"
            + "    alert(rules.length);\n"
            + "    alert(rules[0].selectorText);\n"
            + "    alert(rules[1].selectorText);\n"
            + "  }\n"
            + "}</script>\n"
            + "<style id='myStyle'></style>\n"
            + "</head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test that exception handling in insertRule.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void insertInvalidRule() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var f = document.getElementById('myStyle');\n"
            + "  var s = f.sheet ? f.sheet : f.styleSheet;\n"
            + "  var rules = s.cssRules || s.rules;\n"
            + "  try {\n"
            + "    if (s.insertRule)\n"
            + "      s.insertRule('.testStyle1', 0);\n"
            + "    else\n"
            + "      s.addRule('.testStyle1;', '', 1);\n"
            + "    alert('inserted');\n"
            + "  } catch(err) { alert('exception'); }\n"
            + "}</script>\n"
            + "<style id='myStyle'></style>\n"
            + "</head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "true", "true", "false" },
            IE8 = { "false", "false", "false", "false", "false" },
            CHROME = { "false", "false", "false", "false", "false" })
    public void langCondition() throws Exception {
        final String htmlSnippet = "<div id='elt2' lang='en'></div>\n"
                + "  <div id='elt3' lang='en-GB'></div>\n"
                + "  <div id='elt4' lang='english'></div>\n";
        doTest(":lang(en)", htmlSnippet);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = { "true", "false" },
            IE8 = { "false", "false" },
            CHROME = { "false", "false" })
    public void css2_root() throws Exception {
        doTest(":root", "");
    }

    /**
     * CSS3 pseudo selector :not is not yet supported.
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = { "true", "true", "false" },
            IE8 = { "false", "false", "false" },
            CHROME = { "false", "false", "false" })
    public void css3_not() throws Exception {
        doTest(":not(span)", "<span id='elt2'></span>");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "true", "false", "true", "true", "true", "true" },
            IE8 = { "false", "false", "false", "false", "false", "false", "false", "false" },
            CHROME = { "false", "false", "false", "false", "false", "false", "false", "false" })
    public void css3_enabled() throws Exception {
        final String htmlSnippet = "<input id='elt2'>\n"
            + "<input id='elt3' disabled>\n"
            + "<input id='elt4' type='checkbox'>\n"
            + "<button id='elt5' ></button>\n"
            + "<select id='elt6' ></select>"
            + "<textarea id='elt7' ></textarea>\n";
        doTest(":enabled", htmlSnippet);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "true", "false", "true", "true", "true", "true" },
            IE8 = { "false", "false", "false", "false", "false", "false", "false", "false" },
            CHROME = { "false", "false", "false", "false", "false", "false", "false", "false" })
    public void css3_disabled() throws Exception {
        final String htmlSnippet = "<input id='elt2' disabled>\n"
            + "<input id='elt3'>\n"
            + "<input id='elt4' type='checkbox' disabled>\n"
            + "<button id='elt5' disabled></button>\n"
            + "<select id='elt6' disabled></select>"
            + "<textarea id='elt7' disabled></textarea>\n";
        doTest(":disabled", htmlSnippet);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "false", "false", "true", "false", "true", "false" },
            IE8 = { "false", "false", "false", "false", "false", "false", "false", "false" },
            CHROME = { "false", "false", "false", "false", "false", "false", "false", "false" })
    public void css3_checked() throws Exception {
        final String htmlSnippet = "<input id='elt2'>\n"
            + "<input id='elt3' checked>\n"
            + "<input id='elt4' type='checkbox' checked>\n"
            + "<input id='elt5' type='checkbox'>\n"
            + "<input id='elt6' type='radio' checked>\n"
            + "<input id='elt7' type='radio'>\n";
        doTest(":checked", htmlSnippet);
    }

    private void doTest(final String cssSelector, final String htmlSnippet) throws Exception {
        final String html = "<html id='elt0'><head><title>First</title>\n"
                + "<style>\n"
                + cssSelector + " { z-index: 10 }\n"
                + "</style>\n"
                + "<script>\n"
                + "  function test(){\n"
                + "    var getStyle = function(e) {\n"
                + "      return window.getComputedStyle ? window.getComputedStyle(e,'') : e.currentStyle; };\n"
                + "    var i = 0;\n"
                + "    while (true) {\n"
                + "      var elt = document.getElementById('elt' + i++);\n"
                + "      if (!elt) return;\n"
                + "      alert(getStyle(elt).zIndex == 10);\n"
                + "    }\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "  <div id='elt1'></div>\n"
                + htmlSnippet
                + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test for handling priority !important.
     * @see <a href="http://sf.net/support/tracker.php?aid=2880057">Bug 2880057</a>
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("width=100")
    public void important() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var e = document.getElementById('style1');\n"
            + "  alert('width=' + e.clientWidth);\n"
            + "}\n"
            + "</script>\n"
            + "<style>\n"
            + "#style1 {left: 25px; width: 100px !important;}\n"
            + "#style1 {position: absolute; left: 100px; width: 50px; height: 50px;}\n"
            + "</style>\n"
            + "</head><body onload='doTest()'>\n"
            + "<div id='style1'>Hello</div>"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test for handling/ignoring @font-face.
     * see bug 2984265
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("none")
    public void fontFace() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var e = document.getElementById('div1');\n"
            + "  var s = window.getComputedStyle ? window.getComputedStyle(e,'') : e.currentStyle;\n"
            + "  alert(s.display);\n"
            + "}\n"
            + "</script>\n"
            + "<style>\n"
            + "  @font-face { font-family: YanoneKaffeesatz; src: url(/YanoneKaffeesatz-Regular.otf); }\n"
            + "  body { font-family: YanoneKaffeesatz; }\n"
            + "  div { display: none; }\n"
            + "</style>\n"
            + "</head><body onload='doTest()'>\n"
            + "<div id='div1'>invisible</div>"
            + "visible"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test that the rule with higher specificity wins.
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "60", CHROME = "auto")
    public void rulePriority_specificity() throws Exception {
        final String html = "<html><head>\n"
            + "<style>\n"
            + "div { z-index: 60 }\n"
            + "* { z-index: 10 }\n"
            + "</style></head>\n"
            + "<body>\n"
            + "<div id='it'>hello</div>\n"
            + "<script>\n"
            + "var getStyle = function(e) {\n"
            + "return window.getComputedStyle ? window.getComputedStyle(e,'') : e.currentStyle;\n"
            + "};\n"
            + "alert(getStyle(document.getElementById('it')).zIndex);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test that the rule with higher specificity wins. More complete case.
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "60", CHROME = "auto")
    public void rulePriority_specificity2() throws Exception {
        final String html = "<html><head>\n"
            + "<style>\n"
            + ".classA .classB .classC { z-index: 60 }\n"
            + ".classA .classC { z-index: 10 }\n"
            + "</style></head>\n"
            + "<body>\n"
            + "<div class='classA'>\n"
            + "<div class='classB'>\n"
            + "<div id='it' class='classC'>hello</div>\n"
            + "</div>\n"
            + "</div>\n"
            + "<script>\n"
            + "var getStyle = function(e) {\n"
            + "return window.getComputedStyle ? window.getComputedStyle(e,'') : e.currentStyle;\n"
            + "};\n"
            + "alert(getStyle(document.getElementById('it')).zIndex);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test that the last one wins when selectors have the same specificity.
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = { "10", "10" }, CHROME = { "auto", "auto" })
    public void rulePriority_position() throws Exception {
        final String html = "<html><head>\n"
            + "<style>\n"
            + ".classA { z-index: 60 }\n"
            + ".classB { z-index: 10 }\n"
            + "</style></head>\n"
            + "<body>\n"
            + "<div id='it1' class='classA classB'>hello</div>\n"
            + "<div id='it2' class='classA classB'>hello</div>\n"
            + "<script>\n"
            + "var getStyle = function(e) {\n"
            + "return window.getComputedStyle ? window.getComputedStyle(e,'') : e.currentStyle;\n"
            + "};\n"
            + "alert(getStyle(document.getElementById('it1')).zIndex);\n"
            + "alert(getStyle(document.getElementById('it2')).zIndex);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "none", "1" })
    public void mediaOnStyleTag_noMedia() throws Exception {
        mediaOnStyleTag("");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "none", "1" })
    public void mediaOnStyleTag_whitespace() throws Exception {
        mediaOnStyleTag(" ");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "none", "1" })
    public void mediaOnStyleTag_all() throws Exception {
        mediaOnStyleTag("all");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "none", "1" })
    public void mediaOnStyleTag_screen() throws Exception {
        mediaOnStyleTag("screen");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "block", "1" })
    public void mediaOnStyleTag_notScreen() throws Exception {
        mediaOnStyleTag("print");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "none", "1" })
    public void mediaOnStyleTag_multipleWithScreen() throws Exception {
        mediaOnStyleTag("print, screen, tv");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "block", "1" })
    public void mediaOnStyleTag_multipleWithoutScreen() throws Exception {
        mediaOnStyleTag("print, projection, tv");
    }

    private void mediaOnStyleTag(final String media) throws Exception {
        final String html
            = "<html><head>\n"
            + "<style media='" + media + "'> div { display: none }</style>\n"
            + "</head><body>\n"
            + "<div id='d'>hello</div>\n"
            + "<script>\n"
            + "  var getStyle = function(e) {\n"
            + "    return window.getComputedStyle ? window.getComputedStyle(e,'') : e.currentStyle;\n"
            + "  };\n"
            + "  alert(getStyle(document.getElementById('d')).display);\n"
            + "  alert(document.styleSheets.length);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "none", "1" })
    public void mediaOnLinkTag_noMedia() throws Exception {
        mediaOnLinkTag("");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "none", "1" })
    public void mediaOnLinkTag_whitespace() throws Exception {
        mediaOnLinkTag(" ");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "none", "1" })
    public void mediaOnLinkTag_all() throws Exception {
        mediaOnLinkTag("all");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "none", "1" })
    public void mediaOnLinkTag_screen() throws Exception {
        mediaOnLinkTag("screen");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "block", "1" }, CHROME = { "block", "0" })
    public void mediaOnLinkTag_notScreen() throws Exception {
        mediaOnLinkTag("print");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "none", "1" })
    public void mediaOnLinkTag_multipleWithScreen() throws Exception {
        mediaOnLinkTag("print, screen, tv");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "block", "1" }, CHROME = { "block", "0" })
    public void mediaOnLinkTag_multipleWithoutScreen() throws Exception {
        mediaOnLinkTag("print, projection, tv");
    }

    private void mediaOnLinkTag(final String media) throws Exception {
        final String html
            = "<html><head>\n"
            + "<link rel='stylesheet' media='" + media + "' href='" + URL_SECOND + "'></link>\n"
            + "</head><body>\n"
            + "<div id='d'>hello</div>\n"
            + "<script>\n"
            + "  var getStyle = function(e) {\n"
            + "    return window.getComputedStyle ? window.getComputedStyle(e,'') : e.currentStyle;\n"
            + "  };\n"
            + "  alert(getStyle(document.getElementById('d')).display);\n"
            + "  alert(document.styleSheets.length);\n"
            + "</script></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "div { display: none }", "text/css");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "none", "1" })
    public void mediaRule_screen() throws Exception {
        mediaRule("screen");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "block", "1" })
    public void mediaRule_notScreen() throws Exception {
        mediaRule("print");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "none", "1" })
    public void mediaRule_multipleWithScreen() throws Exception {
        mediaRule("print, screen, tv");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "block", "1" })
    public void mediaRule_multipleWithoutScreen() throws Exception {
        mediaRule("print, projection, tv");
    }

    private void mediaRule(final String media) throws Exception {
        final String html
            = "<html><head>\n"
            + "<style> @media " + media + " { div { display: none } }</style>\n"
            + "</head><body>\n"
            + "<div id='d'>hello</div>\n"
            + "<script>\n"
            + "  var getStyle = function(e) {\n"
            + "    return window.getComputedStyle ? window.getComputedStyle(e,'') : e.currentStyle;\n"
            + "  };\n"
            + "  alert(getStyle(document.getElementById('d')).display);\n"
            + "  alert(document.styleSheets.length);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

}
