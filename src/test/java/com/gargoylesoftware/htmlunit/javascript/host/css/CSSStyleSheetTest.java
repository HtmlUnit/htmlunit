/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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

import java.io.StringReader;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Selector;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLStyleElement;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS21;

/**
 * Unit tests for {@link CSSStyleSheet}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class CSSStyleSheetTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.NONE)
    public void selects_miscSelectors() throws Exception {
        final String html = "<html><head><title>test</title>\n"
            + "</head><body><style></style>\n"
            + "<form name='f1' action='foo' class='yui-log'>\n"
            + "<div><div><input name='i1' id='m1'></div></div>\n"
            + "<input name='i2' class='yui-log'>\n"
            + "<button name='b1' class='yui-log'>\n"
            + "<button name='b2'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlElement body = page.getBody();
        final HtmlForm form = page.getFormByName("f1");
        final HtmlInput input1 = (HtmlInput) page.getElementsByName("i1").get(0);
        final HtmlInput input2 = (HtmlInput) page.getElementsByName("i2").get(0);
        final HtmlElement button1 = page.getElementsByName("b1").get(0);
        final HtmlElement button2 = page.getElementsByName("b2").get(0);

        final HtmlStyle node = (HtmlStyle) page.getElementsByTagName("style").item(0);
        final HTMLStyleElement host = (HTMLStyleElement) node.getScriptObject();
        final CSSStyleSheet sheet = host.jsxGet_sheet();

        Selector selector = parseSelector(sheet, "*.yui-log input");
        assertFalse(sheet.selects(selector, body));
        assertFalse(sheet.selects(selector, form));
        assertTrue(sheet.selects(selector, input1));
        assertTrue(sheet.selects(selector, input2));
        assertFalse(sheet.selects(selector, button1));
        assertFalse(sheet.selects(selector, button2));

        selector = parseSelector(sheet, "#m1");
        assertTrue(sheet.selects(selector, input1));
        assertFalse(sheet.selects(selector, input2));
    }

    private Selector parseSelector(final CSSStyleSheet sheet, final String rule) {
        return sheet.parseSelectors(new InputSource(new StringReader(rule))).item(0);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.NONE)
    public void selects_anyNodeSelector() throws Exception {
        testSelects("*", true, true, true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.NONE)
    public void selects_childSelector() throws Exception {
        testSelects("body > div", false, true, false);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.NONE)
    public void selects_descendantSelector() throws Exception {
        testSelects("body span", false, false, true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.NONE)
    public void selects_elementSelector() throws Exception {
        testSelects("div", false, true, false);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.NONE)
    public void selects_directAdjacentSelector() throws Exception {
        testSelects("span + span", false, false, true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.NONE)
    public void selects_conditionalSelector_idCondition() throws Exception {
        testSelects("span#s", false, false, true);
        testSelects("#s", false, false, true);
        testSelects("span[id=s]", false, false, true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.NONE)
    public void selects_conditionalSelector_classCondition() throws Exception {
        testSelects("div.bar", false, true, false);
        testSelects(".bar", false, true, false);
        testSelects("div[class~=bar]", false, true, false);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.NONE)
    public void selects_pseudoClass_root() throws Exception {
        testSelects(":root", false, false, false);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void selects_pseudoClass_lang() throws Exception {
        if (getBrowserVersion().isFirefox()) {
            testSelects(":lang(en)", false, true, true);
            testSelects(":lang(de)", false, false, false);
        }
        else {
            testSelects(":lang(en)", false, false, false);
            testSelects(":lang(de)", false, false, false);
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.NONE)
    @NotYetImplemented
    public void selects_pseudoClass_negation() throws Exception {
        testSelects(":not(div)", true, false, true);
    }

    private void testSelects(final String css, final boolean selectBody, final boolean selectDivD,
        final boolean selectSpanS) throws Exception {
        final String html =
              "<html><body id='b'><style></style>\n"
            + "<div id='d' class='foo bar' lang='en-GB'>"
            + "<span>x</span>"
            + "<span id='s'>a</span>b</div>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlStyle node = (HtmlStyle) page.getElementsByTagName("style").item(0);
        final HTMLStyleElement host = (HTMLStyleElement) node.getScriptObject();
        final CSSStyleSheet sheet = host.jsxGet_sheet();
        final Selector selector = sheet.parseSelectors(new InputSource(new StringReader(css))).item(0);
        assertEquals(selectBody, sheet.selects(selector, page.getHtmlElementById("b")));
        assertEquals(selectDivD, sheet.selects(selector, page.getHtmlElementById("d")));
        assertEquals(selectSpanS, sheet.selects(selector, page.getHtmlElementById("s")));
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(IE = {"[object]", "undefined", "false", "[object]", "true" },
            FF = {"[object CSSStyleSheet]", "[object HTMLStyleElement]", "true", "undefined", "false" })
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
    @Alerts(FF = { "4", "0", "1", "2", "3", "length", "item" },
            IE = { "4", "length", "0", "1", "2", "3" })
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
    @Alerts(FF = { "4", "§§URL§§style2.css", "§§URL§§style4.css", "null", "null" },
            IE = { "4", "§§URL§§style2.css", "style4.css", "", "" })
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
    @Alerts(FF = { "1", "false", "true", "0", "2", "p" },
            IE = { "1", "true", "false", "-1", "2", "DIV" })
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
     * Test that we have a workaround for a bug in CSSParser.
     * @throws Exception if an error occurs
     * @see #npe_root()
     */
    @Test
    @Alerts("2")
    @Browsers(Browser.FF)
    public void npe() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var f = document.getElementById('myStyle');\n"
            + "  var s = f.sheet ? f.sheet : f.styleSheet;\n"
            + "  var rules = s.cssRules || s.rules;\n"
            + "  s.insertRule('.testStyle { width: 24px; }', 0);\n"
            + "  s.insertRule(' .testStyleDef { height: 42px; }', 0);\n"
            + "  alert(rules.length);\n"
            + "}</script>\n"
            + "<style id='myStyle'></style>\n"
            + "</head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * This seems to be a bug in CSSParser. This test can be removed once the problem in CSSParser is fixed.
     * @see <a href="http://sourceforge.net/tracker/index.php?func=detail&aid=2123264&group_id=82996&atid=567969">
     * CSSParser bug [2123264] NPE in insertRule</a>
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.NONE)
    @NotYetImplemented
    public void npe_root() throws Exception {
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());
        final InputSource source = new InputSource(new StringReader(""));
        final org.w3c.dom.css.CSSStyleSheet ss = parser.parseStyleSheet(source, null, null);

        ss.insertRule(".testStyle", 0);
        ss.insertRule(" .testStyleDef", 0);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(FF = { "false", "false", "true", "true", "false" },
            IE = { "false", "false", "false", "false", "false" })
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
    @Alerts(FF = { "true", "false" }, IE = { "false", "false" })
    public void css2_root() throws Exception {
        doTest(":root", "");
    }

    /**
     * CSS3 pseudo selector :not is not yet supported.
     * @throws Exception on test failure
     */
    @Test
    @NotYetImplemented(Browser.FF)
    @Alerts(FF = { "true", "true", "false" }, IE = { "false", "false", "false" })
    public void css3_not() throws Exception {
        doTest(":not(span)", "<span id='elt2'></span>");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(FF = { "false", "false", "true", "false", "true" }, IE = { "false", "false", "false", "false", "false" })
    public void css3_enabled() throws Exception {
        final String htmlSnippet = "<input id='elt2'>\n"
            + "<input id='elt3' disabled>\n"
            + "<input id='elt4' type='checkbox'>\n";
        doTest(":enabled", htmlSnippet);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(FF = { "false", "false", "false", "false", "true", "false", "true", "false" },
            IE = { "false", "false", "false", "false", "false", "false", "false", "false" })
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
     * @see bug 2984265
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("none")
    @NotYetImplemented
    public void fontFace() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var e = document.getElementById('div1');\n"
            + "  var s = window.getComputedStyle ? window.getComputedStyle(e,'') : e.currentStyle; \n"
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
     * Test that the rule with higher specifity wins.
     * @throws Exception on test failure
     */
    @Test
    @Alerts("60")
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
            + "return window.getComputedStyle ? window.getComputedStyle(e,'') : e.currentStyle; \n"
            + "};\n"
            + "alert(getStyle(document.getElementById('it')).zIndex);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test that the rule with higher specifity wins. More comple case.
     * @throws Exception on test failure
     */
    @Test
    @Alerts("60")
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
            + "return window.getComputedStyle ? window.getComputedStyle(e,'') : e.currentStyle; \n"
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
    @Alerts({ "10", "10" })
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
            + "return window.getComputedStyle ? window.getComputedStyle(e,'') : e.currentStyle; \n"
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
            + "    return window.getComputedStyle ? window.getComputedStyle(e,'') : e.currentStyle; \n"
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
    @Alerts({ "block", "1" })
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
    @Alerts({ "block", "1" })
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
            + "    return window.getComputedStyle ? window.getComputedStyle(e,'') : e.currentStyle; \n"
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
            + "    return window.getComputedStyle ? window.getComputedStyle(e,'') : e.currentStyle; \n"
            + "  };\n"
            + "  alert(getStyle(document.getElementById('d')).display);\n"
            + "  alert(document.styleSheets.length);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

}
