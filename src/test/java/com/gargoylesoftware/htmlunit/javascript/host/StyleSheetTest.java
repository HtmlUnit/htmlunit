/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Unit tests for {@link Stylesheet}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class StyleSheetTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.NONE)
    public void selects_miscSelectors() throws Exception {
        final String html = "<html><head><title>test</title>\n"
            + "</head><body>\n"
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
        final HtmlInput input1 = (HtmlInput) page.getHtmlElementsByName("i1").get(0);
        final HtmlInput input2 = (HtmlInput) page.getHtmlElementsByName("i2").get(0);
        final HtmlElement button1 = page.getHtmlElementsByName("b1").get(0);
        final HtmlElement button2 = page.getHtmlElementsByName("b2").get(0);

        final Stylesheet stylesheet = new Stylesheet();
        Selector selector = parseSelector("*.yui-log input { }");
        assertFalse(stylesheet.selects(selector, body));
        assertFalse(stylesheet.selects(selector, form));
        assertTrue(stylesheet.selects(selector, input1));
        assertTrue(stylesheet.selects(selector, input2));
        assertFalse(stylesheet.selects(selector, button1));
        assertFalse(stylesheet.selects(selector, button2));

        selector = parseSelector("#m1 { margin: 3px; }");
        assertTrue(stylesheet.selects(selector, input1));
        assertFalse(stylesheet.selects(selector, input2));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.NONE)
    public void selects_anyNodeSelector() throws Exception {
        testSelects("* { color: red; }", true, true, true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.NONE)
    public void selects_childSelector() throws Exception {
        testSelects("body > div { color: red; }", false, true, false);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.NONE)
    public void selects_descendantSelector() throws Exception {
        testSelects("body span { color: red; }", false, false, true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.NONE)
    public void selects_elementSelector() throws Exception {
        testSelects("div { color: red; }", false, true, false);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.NONE)
    public void selects_directAdjacentSelector() throws Exception {
        testSelects("span + span { color: red; }", false, false, true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.NONE)
    public void selects_conditionalSelector_idCondition() throws Exception {
        testSelects("span#s { color: red; }", false, false, true);
        testSelects("#s { color: red; }", false, false, true);
        testSelects("span[id=s] { color: red; }", false, false, true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.NONE)
    public void selects_conditionalSelector_classCondition() throws Exception {
        testSelects("div.bar { color: red; }", false, true, false);
        testSelects(".bar { color: red; }", false, true, false);
        testSelects("div[class~=bar] { color: red; }", false, true, false);
    }

    private void testSelects(final String css, final boolean b, final boolean d, final boolean s) throws Exception {
        final String html =
              "<html><body id='b'>\n"
            + "<div id='d' class='foo bar'><span>x</span><span id='s'>a</span>b</div>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(html);
        final Selector selector = parseSelector(css);
        final Stylesheet sheet = new Stylesheet();
        assertEquals(b, sheet.selects(selector, page.getHtmlElementById("b")));
        assertEquals(d, sheet.selects(selector, page.getHtmlElementById("d")));
        assertEquals(s, sheet.selects(selector, page.getHtmlElementById("s")));
    }

    private Selector parseSelector(final String rule) {
        final Stylesheet stylesheet = new Stylesheet();
        final SelectorList selectors = stylesheet.parseSelectors(new InputSource(new StringReader(rule)));
        return selectors.item(0);
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
        loadPageWithAlerts(html);
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
        loadPageWithAlerts(html);
    }

    /**
     * Test for bug 2063012 (missing href attribute).
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF2 = { "4", "http://x/style2.css", "http://x/style4.css", "http://x/test.html", "http://x/test.html" },
            FF3 = { "4", "http://x/style2.css", "http://x/style4.css", "null", "null" },
            IE = { "4", "http://x/style2.css", "style4.css", "", "" })
    public void href() throws Exception {
        final String html = "<html>\n"
            + "  <head>\n"
            + "    <link href='http://x/style1.css' type='text/css'></link>\n" // Ignored.
            + "    <link href='http://x/style2.css' rel='stylesheet'></link>\n"
            + "    <link href='http://x/style3.css'></link>\n" // Ignored.
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

        final WebClient client = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection conn = new MockWebConnection(client);
        conn.setResponse(new URL("http://x/test.html"), html);
        conn.setResponse(new URL("http://x/style1.css"), "");
        conn.setResponse(new URL("http://x/style2.css"), "");
        conn.setResponse(new URL("http://x/style3.css"), "");
        conn.setResponse(new URL("http://x/style4.css"), "");
        client.setWebConnection(conn);

        client.getPage("http://x/test.html");
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

}
