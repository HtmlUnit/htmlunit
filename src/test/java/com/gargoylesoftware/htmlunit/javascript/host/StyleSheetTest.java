/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import java.io.StringReader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
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
    public void selects() throws Exception {
        final String html = "<html><head><title>test</title>\n"
            + "</head><body>\n"
            + "<form name='f1' class='foo' class='yui-log'>"
            + "<div><div><input name='i1' id='m1'></div></div>"
            + "<input name='i2' class='yui-log'>"
            + "<button name='b1' class='yui-log'>"
            + "<button name='b2'>"
            + "</form>"
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
                + "function test(){"
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
    @Alerts("4")
    @NotYetImplemented
    public void rules() throws Exception {
        final String html = "<html><head><title>First</title>\n"
                + "<style>\n"
                + "  BODY { background-color: white; color: black; }\n"
                + "  H1 { font: 8pt Arial bold; }\n"
                + "  P  { font: 10pt Arial; text-indent: 0.5in; }\n"
                + "  A  { text-decoration: none; color: blue; }\n"
                + "</style>\n"
                + "<script>\n"
                + "  function test(){"
                + "    if (document.styleSheets[0].cssRules)\n"
                + "      alert(document.styleSheets[0].cssRules.length);\n"
                + "    else\n"
                + "      alert(document.styleSheets[0].rules.length);\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "</body></html>";

        loadPageWithAlerts(html);
    }
}
